package cab.snapp.warehouse.service;

import cab.snapp.warehouse.service.model.ValidationException;
import cab.snapp.warehouse.to.ArticleTo;
import java.util.List;
import org.json.simple.JSONArray;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService extends CrudService<ArticleTo, Long> {


  List<ArticleTo> importArticles(MultipartFile inventory) throws ValidationException;

  List<ArticleTo> articleParser(JSONArray articleJsonArray) throws ValidationException;

  List<ArticleTo> getAllArticles();

}
