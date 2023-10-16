package kitchenpos.table_group.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.table_group.domain.TableGroup;

public interface TableGroupDao {

  TableGroup save(TableGroup entity);

  Optional<TableGroup> findById(Long id);

  List<TableGroup> findAll();
}
