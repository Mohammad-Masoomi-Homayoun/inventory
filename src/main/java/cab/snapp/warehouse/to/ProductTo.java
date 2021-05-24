package cab.snapp.warehouse.to;


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

  private String name;
  private List<ArticleTo> articleToList = new ArrayList<>();
}
