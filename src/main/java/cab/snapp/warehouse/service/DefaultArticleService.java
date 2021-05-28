package cab.snapp.warehouse.service;

import cab.snapp.warehouse.service.mapper.ArticleMapper;
import cab.snapp.warehouse.service.model.Article;
import cab.snapp.warehouse.service.model.NotFoundException;
import cab.snapp.warehouse.service.model.ValidationException;
import cab.snapp.warehouse.to.ArticleTo;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Service
@Validated
@Log4j2
public class DefaultArticleService implements ArticleService {

  @Resource
  private ArticleMapper articleMapper;
  @Resource
  private ArticleRepository articleRepository;

  @Override
  public List<ArticleTo> createAll(@Valid List<ArticleTo> articleToList)
      throws ValidationException {

    List<ArticleTo> result = new ArrayList<>();
    for (ArticleTo articleTo : articleToList) {
      if (isProductArticle(articleTo)) {
        result.add(create(articleTo));
      } else {
        Article oldArticle = articleRepository.findByArticleId(articleTo.getArticleId());
        if (oldArticle == null) {
          result.add(create(articleTo));
        } else {
          oldArticle.setName(articleTo.getName());
          oldArticle.setStock(articleTo.getStock());
          result.add(articleMapper.mapToDto(articleRepository.save(oldArticle)));
        }
      }
    }

    return result;
  }

  private Boolean isProductArticle(ArticleTo articleTo) {

    return (articleTo.getName() == null && articleTo.getStock() == null && articleTo.getAmount() != null);
  }

  @Override
  public ArticleTo create(@Valid ArticleTo articleTo) throws ValidationException {

    if (articleTo == null) {
      throw new IllegalArgumentException("Article could not be null");
    }

    Article article = new Article(null, articleTo.getArticleId(), articleTo.getName(),
        articleTo.getStock(), articleTo.getAmount());

    return articleMapper.mapToDto(articleRepository.save(article));
  }

  @Override
  public ArticleTo get(Long id) throws NotFoundException {

    return articleMapper.mapToDto(findById(id));
  }

  @Override
  public ArticleTo update(@Valid ArticleTo articleTo) {

    if (articleTo == null) {
      throw new IllegalArgumentException("Article could not be null");
    }

    Article article = articleMapper.mapToDomain(articleTo);
    article = articleRepository.save(article);
    return articleMapper.mapToDto(article);
  }

  @Override
  public ArticleTo delete(Long id) throws NotFoundException {

    Article article = findById(id);

    articleRepository.delete(article);

    return articleMapper.mapToDto(article);
  }

  @Override
  public List<ArticleTo> importArticles(MultipartFile inventory) {

    List<ArticleTo> articleList = inventoryParser(inventory);
    return createAll(articleList);
  }

  public List<ArticleTo> inventoryParser(MultipartFile file) {

    if (file == null) {
      throw new ValidationException("File not inserted", HttpStatus.BAD_REQUEST, 40005);
    }

    List<ArticleTo> articleList;
    JSONParser jsonParser = new JSONParser();
    try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
      JSONObject obj = (JSONObject) jsonParser.parse(reader);
      articleList = articleParser((JSONArray) obj.get("inventory"));
    } catch (IOException | ParseException e) {
      throw new ValidationException("File is not in expected format", HttpStatus.BAD_REQUEST,
          40002);
    }

    return articleList;
  }

  @Override
  public List<ArticleTo> articleParser(JSONArray articlesJsonArray) throws ValidationException {

    List<ArticleTo> articleToList = new ArrayList<>();
    for (Object article : articlesJsonArray) {
      articleToList.add(articleMaker((JSONObject) article));
    }

    return articleToList;
  }

  @Override
  public List<ArticleTo> getAllArticles() {

    return articleMapper
        .mapToDtoList(articleRepository.findAllByArticleIdNotNullAndNameNotNullAndStockNotNull());
  }

  @Override
  @Transactional
  public void sell(List<ArticleTo> articleToList) {

    for (ArticleTo article : articleToList) {
      Article persistArticle;
      try {
        persistArticle = articleRepository.findByArticleId(article.getArticleId());
      } catch (Exception e) {
        log.debug(e.getMessage());
        throw new ValidationException("Beacuse of Database errors can't complete this transaction!",
            HttpStatus.INTERNAL_SERVER_ERROR, 50003);
      }
      if (persistArticle == null || persistArticle.getStock() < article.getAmount()) {
        throw new ValidationException("There is not enough stock in db!",
            HttpStatus.NOT_FOUND, 50004);
      }
      persistArticle.setStock(persistArticle.getStock() - article.getAmount());
      articleRepository.save(persistArticle);
    }
  }

  private ArticleTo articleMaker(JSONObject article) throws ValidationException {

    if (article == null) {
      return null;
    }

    ArticleTo articleTo = new ArticleTo();

    if (article.get("articleId") == null) {
      throw new ValidationException("Article should has id", HttpStatus.BAD_REQUEST, 40005);
    }
    articleTo.setArticleId(Long.parseLong(article.get("articleId").toString()));

    if (article.get("name") != null) {
      articleTo.setName(article.get("name").toString());
    }

    if (article.get("stock") != null) {
      articleTo.setStock(Integer.parseInt(article.get("stock").toString()));
    }

    if (article.get("amount") != null) {
      articleTo.setAmount(Integer.parseInt(article.get("amount").toString()));
    }

    return articleTo;
  }


  private Article findById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Article id could not be null");
    }

    log.debug("Finding article {} by id", id);
    Article article = articleRepository.findById(id)
        .orElseThrow(this::articleNotFoundException);
    log.debug("Article {} is found!", id);
    return article;
  }

  private NotFoundException articleNotFoundException() {
    return new NotFoundException("Can't find article with this id", HttpStatus.NOT_FOUND, 40401);
  }
}
