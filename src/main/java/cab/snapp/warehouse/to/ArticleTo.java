package cab.snapp.warehouse.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleTo {

  @NotNull
  @JsonProperty("article_id")
  private Long articleId;
  private String name;
  private Integer stock;
  private Integer amount;

}
