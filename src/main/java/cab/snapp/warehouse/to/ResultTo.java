package cab.snapp.warehouse.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultTo {

  private Long id;
  @JsonProperty("total_count")
  private Integer totalCount = 0;
  private Integer count = 0;
  private Object body = null;


  public ResultTo(Object body) {
    this.body = body;
  }
}
