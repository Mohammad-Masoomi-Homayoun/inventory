package cab.snapp.warehouse.service;

import cab.snapp.warehouse.matcher.ProductMatcher;
import cab.snapp.warehouse.service.mapper.ArticleMapper;
import cab.snapp.warehouse.service.mapper.ProductMapper;
import cab.snapp.warehouse.service.model.NotFoundException;
import cab.snapp.warehouse.service.model.Product;
import cab.snapp.warehouse.service.model.ValidationException;
import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.MatchedProductTo;
import cab.snapp.warehouse.to.ProductTo;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
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
public class DefaultProductService implements ProductService {

  @Resource
  private ProductMapper productMapper;
  @Resource
  private ArticleMapper articleMapper;
  @Resource
  private ProductRepository productRepository;
  @Resource
  private ArticleService articleService;
  @Resource
  private ProductMatcher productMatcher;

  @Override
  public List<ProductTo> createAll(@Valid List<ProductTo> productToList)
      throws ValidationException {

    List<Product> productList = productMapper.mapToDomainList(productToList);

    return productMapper.mapToDtoList(productRepository.saveAll(productList));
  }

  @Override
  public ProductTo create(@Valid ProductTo productTo) throws ValidationException {

    if (productTo == null) {
      throw new IllegalArgumentException("Product could not be null");
    }

    Product product = new Product(productTo.getName(),
        articleMapper.mapToDomainList(productTo.getArticleToList()));

    return productMapper.mapToDto(productRepository.save(product));
  }

  @Override
  public ProductTo get(Long id) throws NotFoundException {

    return productMapper.mapToDto(findById(id));
  }

  @Override
  public ProductTo update(@Valid ProductTo productTo) {

    if (productTo == null) {
      throw new IllegalArgumentException("Product could not be null");
    }

    Product product = productMapper.mapToDomain(productTo);
    product = productRepository.save(product);
    return productMapper.mapToDto(product);
  }

  @Override
  public ProductTo delete(Long id) throws NotFoundException {

    Product product = findById(id);

    productRepository.delete(product);

    return productMapper.mapToDto(product);
  }

  @Override
  public List<ProductTo> importProducts(MultipartFile products) throws ValidationException {

    List<ProductTo> productList = productParser(products);
    return createAll(productList);
  }

  @Override
  public List<MatchedProductTo> getAllProducts() {

    List<ProductTo> products = productMapper.mapToDtoList(productRepository.findAll());
    List<ArticleTo> articles = articleService.getAllArticles();

    return productMatcher.match(products, articles);
  }

  @Override
  public ProductTo sellProduct(Long id) {

    ProductTo productTo = productMapper.mapToDto(findById(id));
    articleService.sell(productTo.getArticleToList());
    return productTo;
  }

  private List<ProductTo> productParser(MultipartFile file) throws ValidationException {

    if (file == null) {
      return null;
    }

    List<ProductTo> productList = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
      JSONObject obj = (JSONObject) jsonParser.parse(reader);
      for (Object product : (JSONArray) obj.get("products")) {
        productList.add(productMaker((JSONObject) product));
      }
    } catch (IOException | ParseException e) {
      throw new ValidationException("File is not in expected format", HttpStatus.BAD_REQUEST,
          40003);
    }

    return productList;
  }

  private ProductTo productMaker(JSONObject product) throws ValidationException {

    if (product == null) {
      return null;
    }

    ProductTo productTo = new ProductTo();

    if (product.get("name") != null) {
      productTo.setName(product.get("name").toString());
    }

    if (product.get("articles") != null) {
      productTo.getArticleToList()
          .addAll(articleService.articleParser((JSONArray) product.get("articles")));
    }

    return productTo;
  }


  private Product findById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Product id could not be null");
    }

    log.debug("Finding product {} by id", id);
    Product product = productRepository.findById(id)
        .orElseThrow(this::productNotFoundException);
    log.debug("Product {} is found!", id);
    return product;
  }

  private NotFoundException productNotFoundException() {
    return new NotFoundException("Can't find product with this id", HttpStatus.NOT_FOUND, 40401);
  }
}
