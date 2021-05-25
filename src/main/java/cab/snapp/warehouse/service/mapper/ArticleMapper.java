package cab.snapp.warehouse.service.mapper;

import cab.snapp.warehouse.service.model.Article;
import cab.snapp.warehouse.to.ArticleTo;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper implements BaseDtoDomainMapper<ArticleTo, Article> {

  @Override
  public ArticleTo mapToDto(Article article) {
    if (article == null) {
      return null;
    }

    return new ArticleTo(article.getArticleId(), article.getName(), article.getStock(),
        article.getAmount());
  }

  @Override
  public Article mapToDomain(ArticleTo articleTo) {
    if (articleTo == null) {
      return null;
    }

    return new Article(null, articleTo.getArticleId(), articleTo.getName(), articleTo.getStock(),
        articleTo.getAmount());
  }
}
