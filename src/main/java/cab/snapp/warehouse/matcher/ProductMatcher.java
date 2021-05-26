package cab.snapp.warehouse.matcher;

import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.MatchedProductTo;
import cab.snapp.warehouse.to.ProductTo;
import java.util.List;

public interface ProductMatcher {

  List<MatchedProductTo> match(List<ProductTo> products, List<ArticleTo> articles);

}
