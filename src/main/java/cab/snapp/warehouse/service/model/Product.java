package cab.snapp.warehouse.service.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  private String name;
  @Column(name = "article_id")
  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  private List<Article> articleList = new ArrayList<>();

  public Product(String name, List<Article> articleList) {

    this.name = name;
    if (articleList.size() > 0) {
      this.getArticleList().addAll(articleList);
    }
  }
}
