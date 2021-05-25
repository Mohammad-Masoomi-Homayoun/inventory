package cab.snapp.warehouse.controller;

import cab.snapp.warehouse.service.ArticleService;
import cab.snapp.warehouse.service.model.ValidationException;
import cab.snapp.warehouse.to.ArticleTo;
import cab.snapp.warehouse.to.ResultTo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/articles")
public class ArticleController extends BaseController<ArticleTo, Long, ArticleService> {

  @PostMapping("/import")
  public ResultTo importArticles(@RequestParam("inventory") MultipartFile inventory) {

    if (inventory == null) {
      throw new ValidationException("Request should include inventory file", HttpStatus.BAD_REQUEST,
          40001);
    }

    return createResultTo(service.importArticles(inventory));
  }

}
