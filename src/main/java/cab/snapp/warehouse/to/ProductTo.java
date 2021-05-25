package cab.snapp.warehouse.to;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductTo {

  @JsonIgnore
  private Long id;
  private String name;
  @JsonProperty("articles")
  private List<ArticleTo> articleToList = new ArrayList<>();
}
