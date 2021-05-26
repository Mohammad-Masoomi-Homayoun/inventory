package cab.snapp.warehouse.to;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchedProductTo {

  private Long id;
  private String product;
  private Integer available;

  public void increase() {
    this.available++;
  }
}
