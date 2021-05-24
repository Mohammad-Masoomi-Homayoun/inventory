package cab.snapp.warehouse.service.mapper;

import static java.util.stream.Collectors.toList;

import java.util.List;

public interface BaseDtoDomainMapper<TO, DOMAIN> {

  default List<DOMAIN> mapToDomainList(final List<TO> toList) {

    return toList == null ? null : toList.stream().map(this::mapToDomain).collect(toList());
  }

  default List<TO> mapToDtoList(final List<DOMAIN> domains) {

    return domains == null ? null : domains.stream().map(this::mapToDto).collect(toList());
  }


  TO mapToDto(DOMAIN domain);

  DOMAIN mapToDomain(TO to);
}
