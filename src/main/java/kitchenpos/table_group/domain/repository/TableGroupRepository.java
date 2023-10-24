package kitchenpos.table_group.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.table_group.domain.TableGroup;

public interface TableGroupRepository {

  TableGroup save(TableGroup tableGroup);

  Optional<TableGroup> findById(Long id);

  List<TableGroup> findAll();
}
