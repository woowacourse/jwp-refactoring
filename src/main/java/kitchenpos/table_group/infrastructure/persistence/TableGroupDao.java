package kitchenpos.table_group.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

public interface TableGroupDao {

  TableGroupEntity save(TableGroupEntity entity);

  Optional<TableGroupEntity> findById(Long id);

  List<TableGroupEntity> findAll();
}
