package cab.snapp.warehouse.service;

import cab.snapp.warehouse.service.model.ValidationException;
import cab.snapp.warehouse.to.MatchedProductTo;
import cab.snapp.warehouse.to.ProductTo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService extends CrudService<ProductTo, Long> {

  List<ProductTo> importProducts(MultipartFile products) throws ValidationException;

  List<MatchedProductTo> getAllProducts();

  ProductTo sellProduct(Long id);
}
