package cab.snapp.warehouse.matcher;

import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.MatchedProductTo;
import cab.snapp.warehouse.to.ProductTo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DefaultProductMatcher implements ProductMatcher {

  @Override
  public List<MatchedProductTo> match(List<ProductTo> products, List<ArticleTo> articles) {

    if (articles == null || articles.isEmpty() || products == null || products.isEmpty()) {
      return null;
    }

    Map<String, Integer> matchedProducts = new HashMap<>();

    Map<Long, Integer> articlesAsset = new HashMap<>();
    for (ArticleTo article : articles) {
      articlesAsset.put(article.getArticleId(), article.getStock());
    }

    // Round Robin match products
    int counter = 0;
    while (products.size() > 0) {
      ProductTo product = products.get(counter);
      if (canMakeProductThenMake(product.getArticleToList(), articlesAsset)) {
        matchedProducts.put(product.getName(), matchedProducts.getOrDefault(product.getName(), 0) + 1);
      } else {
        products.remove(counter);
      }
      counter++;
      if (counter == products.size()) {
        counter = 0;
      }
    }

    return resultConvertor(matchedProducts);
  }

  private List<MatchedProductTo> resultConvertor(Map matchedProducts) {

    List<MatchedProductTo> matchedProductToList = new ArrayList<>();
    Iterator matchIterator = matchedProducts.entrySet().iterator();
    while (matchIterator.hasNext()) {
      Map.Entry pair = (Map.Entry) matchIterator.next();
      matchedProductToList
          .add(new MatchedProductTo(pair.getKey().toString(), (Integer) pair.getValue()));
      matchIterator.remove();
    }
    return matchedProductToList;
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
