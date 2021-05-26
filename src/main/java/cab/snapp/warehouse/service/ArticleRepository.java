package cab.snapp.warehouse.service;

import cab.snapp.warehouse.service.model.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  List<Article> findAllByArticleIdNotNullAndNameNotNullAndStockNotNull();

  @Query("SELECT article FROM Article article WHERE article.articleId = :articleId AND article.name is not null AND article.stock is not null")
  Article findByArticleId(Long articleId);
}
