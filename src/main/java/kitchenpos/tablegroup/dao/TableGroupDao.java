package kitchenpos.tablegroup.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.tablegroup.domain.TableGroup;

public interface TableGroupDao {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
