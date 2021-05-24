package cab.snapp.warehouse.controller;

import cab.snapp.warehouse.service.CrudService;
import cab.snapp.warehouse.to.ResultTo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController<TO, ID, SERVICE extends CrudService<TO, ID>> {

  @Autowired
  protected SERVICE service;

  public ResultTo createResultTo(Object data) {
    return createResultTo(data, "Error code not set!");
  }

  public ResultTo createResultTo(Object data, String error) {
    return createResultTo(data, error, 0);
  }

  public ResultTo createResultTo(Object data, String error, Integer totalCount) {
    ResultTo ResultTo = new ResultTo();

    if (data != null) {

      ResultTo.setBody(data);
      ResultTo.setTotalCount(totalCount);

      if (data instanceof List) {
        ResultTo.setCount(((List) data).size());
      } else {
        ResultTo.setCount(1);
      }

      if (totalCount == 0) {
        ResultTo.setTotalCount(ResultTo.getCount());
      }

    } else {
      ResultTo.setBody(error);
      ResultTo.setTotalCount(0);
      ResultTo.setCount(0);
    }

    return ResultTo;
  }

  public ResultTo createResultTo(Object data, String error, Boolean empty) {

    if (empty) {
      data = new ArrayList<>();
    }
    return createResultTo(data, error);
  }

}
