package cab.snapp.warehouse.service;

import cab.snapp.warehouse.to.ArticleTo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService extends CrudService<ArticleTo, Long> {


  List<ArticleTo> importArticles(MultipartFile inventory);
}
