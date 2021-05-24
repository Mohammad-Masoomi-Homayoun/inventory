package cab.snapp.warehouse.to;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorTo {

  private String message;
  private Integer code;
  private List<Object> paramList;

  public ErrorTo(String message, Integer code) {
    this.message = message;
    this.code = code;
  }

}