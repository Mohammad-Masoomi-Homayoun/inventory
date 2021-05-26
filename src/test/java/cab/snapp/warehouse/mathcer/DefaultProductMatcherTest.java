package cab.snapp.warehouse.mathcer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

import cab.snapp.warehouse.matcher.DefaultProductMatcher;
import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.ProductTo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class DefaultProductMatcherTest {

  private DefaultProductMatcher defaultProductMatcher;
  private Map<Long, Integer> assets = new HashMap<>();
  private List<ArticleTo> articles = new ArrayList<>();

  @Before
  public void init() {
    defaultProductMatcher = new DefaultProductMatcher();
  }

  @Test
  public void enoughSourceToMake_ValidInputs_ReturnTrue() {

    articles.add(new ArticleTo(1l, "leg", null, 10));
    articles.add(new ArticleTo(2l, "screw", null, 20));

    assets.put(1l, 30);
    assets.put(2l, 40);

    assertEquals(true, defaultProductMatcher.canMakeProductThenMake(articles, assets));
  }

  @Test
  public void enoughSourceToMake_InvalidInput_ReturnFalse() {

    articles.add(new ArticleTo(1l, "leg", null, 10));
    articles.add(new ArticleTo(2l, "screw", null, 20));

    assets.put(1l, 30);
    assets.put(2l, 5);

    assertEquals(false, defaultProductMatcher.canMakeProductThenMake(articles, null));
    assertEquals(false, defaultProductMatcher.canMakeProductThenMake(null, assets));
    assertEquals(false, defaultProductMatcher.canMakeProductThenMake(articles, assets));
  }

  @Test
  public void match_InputEnoughArticleAndProduct_ReturnAMatchedProduct() {

    articles.add(new ArticleTo(1l, "leg", 10, null));
    articles.add(new ArticleTo(2l, "screw", 20, null));
    articles.add(new ArticleTo(3l, "table top", 3, null));

    List<ArticleTo> productArticles = new ArrayList<>();
    productArticles.add(new ArticleTo(1l, null, null, 4));
    productArticles.add(new ArticleTo(2l, null, null, 8));
    productArticles.add(new ArticleTo(3l, null, null, 1));
    ProductTo product = new ProductTo();
    product.setId(1l);
    product.setName("Table");
    product.setArticleToList(productArticles);

    assertThat(defaultProductMatcher.match(new ArrayList<>(Arrays.asList(product)), articles),
        is(notNullValue()));
    assertThat(defaultProductMatcher.match(new ArrayList<>(Arrays.asList(product)), articles),
        is(notNullValue()));
    assertThat(
        defaultProductMatcher.match(new ArrayList<>(Arrays.asList(product)), articles).get(0),
        hasProperty("product", is("Table")));
    assertThat(
        defaultProductMatcher.match(new ArrayList<>(Arrays.asList(product)), articles).get(0),
        hasProperty("available", is(2)));
  }

  @Test
  public void match_LessArticlesThanProductNeed_ReturnEmptyArray() {

    articles.add(new ArticleTo(1l, "leg", 1, null));
    articles.add(new ArticleTo(2l, "screw", 1, null));
    articles.add(new ArticleTo(3l, "table top", null, null));

    List<ArticleTo> productArticles = new ArrayList<>();
    productArticles.add(new ArticleTo(1l, null, null, 4));
    productArticles.add(new ArticleTo(2l, null, null, 8));
    productArticles.add(new ArticleTo(3l, null, null, 1));
    ProductTo product = new ProductTo();
    product.setId(1l);
    product.setName("Table");
    product.setArticleToList(productArticles);

    assertThat(defaultProductMatcher.match(new ArrayList<>(Arrays.asList(product)), articles),
        is(empty()));
  }
}
