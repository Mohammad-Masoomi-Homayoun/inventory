package cab.snapp.warehouse.matcher;

import cab.snapp.warehouse.service.model.ValidationException;
import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.MatchedProductTo;
import cab.snapp.warehouse.to.ProductTo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DefaultProductMatcher implements ProductMatcher {

  @Override
  public List<MatchedProductTo> match(List<ProductTo> products, List<ArticleTo> articles) {

    if (articles == null || articles.isEmpty() || products == null || products.isEmpty()) {
      return null;
    }

    Map<Long, Integer> articlesAsset = new HashMap<>();
    for (ArticleTo article : articles) {
      articlesAsset.put(article.getArticleId(), article.getStock());
    }

    return roundRobinMatch(products, articlesAsset);
  }

  public List<MatchedProductTo> roundRobinMatch(List<ProductTo> products, Map articlesAsset) {

    Map<Long, MatchedProductTo> matchedProducts = new HashMap<>();
    int counter = 0;

    try {
      while (products.size() > 0) {
        ProductTo product = products.get(counter);
        if (canMakeProductThenMake(product.getArticleToList(), articlesAsset)) {
          updateMatchedProducts(matchedProducts, product);
        } else {
          products.remove(counter);
        }
        counter++;
        if (counter >= products.size()) {
          counter = 0;
        }
      }
    } catch (Exception e) {
      log.debug(e.getMessage());
      throw new ValidationException("Problem in RoundRobin matching products",
          HttpStatus.INTERNAL_SERVER_ERROR, 50004);
    }

    return matchedProducts.values().stream().collect(Collectors.toList());
  }

  private void updateMatchedProducts(Map<Long, MatchedProductTo> matchedProducts,
      ProductTo product) {
    MatchedProductTo matchedProductTo = matchedProducts.get(product.getId());
    if (matchedProductTo == null) {
      matchedProducts
          .put(product.getId(), new MatchedProductTo(product.getId(), product.getName(), 1));
    } else {
      matchedProductTo.increase();
      matchedProducts.put(product.getId(), matchedProductTo);
    }
  }

  public Boolean canMakeProductThenMake(List<ArticleTo> requiredArticles, Map source) {

    if (requiredArticles == null || requiredArticles.isEmpty() || source == null || source
        .isEmpty()) {
      return false;
    }

    for (ArticleTo articleTo : requiredArticles) {
      Integer assetCount = (Integer) source.get(articleTo.getArticleId());
      if (assetCount == null || articleTo.getAmount() > assetCount) {
        return false;
      }
    }

    withdrawArticles(requiredArticles, source);
    return true;
  }

  private void withdrawArticles(List<ArticleTo> requiredArticles, Map source) {

    for (ArticleTo articleTo : requiredArticles) {
      Integer assetCount = (Integer) source.get(articleTo.getArticleId());
      source.put(articleTo.getArticleId(), assetCount - articleTo.getAmount());
    }
  }
}
