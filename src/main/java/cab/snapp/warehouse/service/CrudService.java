package cab.snapp.warehouse.service;

import cab.snapp.warehouse.service.model.NotFoundException;
import cab.snapp.warehouse.service.model.ValidationException;
import java.util.List;
import javax.validation.Valid;

public interface CrudService<TO, ID> {

  List<TO> createAll(@Valid List<TO> to) throws ValidationException;

  TO create(@Valid TO to) throws ValidationException;

  TO get(ID id) throws NotFoundException;

  TO update(@Valid TO to);

  TO delete(ID id) throws NotFoundException;
}
