package kitchenpos.domain;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository {

  TableGroup save(TableGroup tableGroup);

  Optional<TableGroup> findById(Long id);

  List<TableGroup> findAll();
}
