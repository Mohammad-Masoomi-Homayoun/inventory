package cab.snapp.warehouse.service;


import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cab.snapp.warehouse.to.ArticleTo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

public class DefaultArticleServiceTest {

  private DefaultArticleService defaultArticleService;

  @Before
  public void setUp() {
    defaultArticleService = new DefaultArticleService();
  }

  @Test
  public void sell_ValidArticles_ChangeTheRepositoryState() {

  }

  @Test
  public void inventoryParser_ValidFile_ReturnArticleList() throws IOException {

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("inventory.json").getFile());
    List<ArticleTo> articles = defaultArticleService
        .inventoryParser(new MockMultipartFile("inventory.json", new FileInputStream(file)));
    assertNotNull(articles);
    assertThat(articles, is(not(empty())));
    assertThat(articles.get(0), hasProperty("articleId", is(1l)));
    assertThat(articles.get(0), hasProperty("name", is("leg")));
    assertThat(articles.get(0), hasProperty("stock", is(16)));
  }

}
