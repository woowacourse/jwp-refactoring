package kitchenpos.dao.table;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.table.TableGroup;

public interface TableGroupDao {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
