package cab.snapp.warehouse.service;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.ProductTo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

public class DefaultProductServiceTest {

  @InjectMocks
  private DefaultProductService defaultProductService;
  @Mock
  private ArticleService articleService;

  @Before
  public void setUp() {
    defaultProductService = new DefaultProductService();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void productParserTest_ValidInput_ReturnProductList() throws IOException, ParseException {

    String data = "{\"articles\": [{\"articleId\": \"1\", \"amount\": 4 }, { \"articleId\": \"2\", \"amount\": 8 }, { \"articleId\": \"3\", \"amount\": 1}]}";
    JSONParser parser = new JSONParser();
    JSONObject articlesObject = (JSONObject) parser.parse(data);
    when(articleService.articleParser((JSONArray) articlesObject.get("articles")))
        .thenReturn(Arrays.asList(new ArticleTo(1l, null, null, 4)));

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("products.json").getFile());
    List<ProductTo> products = defaultProductService
        .productParser(new MockMultipartFile("products.json", new FileInputStream(file)));
    assertNotNull(products);
    assertThat(products, is(not(empty())));
    assertThat(products.get(0), hasProperty("name", is("Dining Chair")));
    assertThat(products.get(0).getArticleToList().get(0), hasProperty("articleId", is(1l)));
    assertThat(products.get(0).getArticleToList().get(0), hasProperty("amount", is(4)));
  }

}
