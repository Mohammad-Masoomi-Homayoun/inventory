package cab.snapp.warehouse.service;

import cab.snapp.warehouse.service.model.NotFoundException;
import cab.snapp.warehouse.service.model.ValidationException;
import javax.validation.Valid;

public interface CrudService<TO, ID> {

  TO create(@Valid TO to) throws ValidationException;

  TO get(ID id) throws NotFoundException;

  TO update(@Valid TO to);

  TO delete(ID id) throws NotFoundException;
}
