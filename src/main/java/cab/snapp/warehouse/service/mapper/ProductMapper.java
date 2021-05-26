package cab.snapp.warehouse.service.mapper;

import cab.snapp.warehouse.service.model.Article;
import cab.snapp.warehouse.service.model.Product;
import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.ProductTo;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements BaseDtoDomainMapper<ProductTo, Product> {

  @Resource
  private ArticleMapper articleMapper;

  @Override
  public ProductTo mapToDto(Product product) {
    if (product == null) {
      return null;
    }

    ProductTo productTo = new ProductTo();
    productTo.setId(product.getId());
    productTo.setName(product.getName());
    List<ArticleTo> articles = articleMapper.mapToDtoList(product.getArticleList());
    for (ArticleTo article : articles) {
      productTo.getArticleToList().add(article);
    }

    return productTo;
  }

  @Override
  public Product mapToDomain(ProductTo productTo) {
    if (productTo == null) {
      return null;
    }

    Product product = new Product();
    product.setName(productTo.getName());
    List<Article> articles = articleMapper.mapToDomainList(productTo.getArticleToList());
    for (Article article : articles) {
      product.getArticleList().add(article);
    }

    return product;
  }
}
