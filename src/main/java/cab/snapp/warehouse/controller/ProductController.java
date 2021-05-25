package cab.snapp.warehouse.controller;

import cab.snapp.warehouse.service.ProductService;
import cab.snapp.warehouse.service.model.ValidationException;
import cab.snapp.warehouse.to.ProductTo;
import cab.snapp.warehouse.to.ResultTo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductController extends BaseController<ProductTo, Long, ProductService> {

  @GetMapping
  public ResultTo getProducts() {
    return createResultTo(service.getAllProducts());
  }

  @PostMapping("/import")
  public ResultTo importProducts(@RequestParam("products") MultipartFile products) {

    if (products == null) {
      throw new ValidationException("Request should include products file", HttpStatus.BAD_REQUEST,
          40004);
    }

    return createResultTo(service.importProducts(products));
  }

}