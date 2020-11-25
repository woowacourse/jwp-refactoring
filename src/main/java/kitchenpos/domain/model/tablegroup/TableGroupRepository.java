package kitchenpos.domain.model.tablegroup;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
