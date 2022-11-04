package kitchenpos.table.domain;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository {

    TableGroup save(final TableGroup entity);

    Optional<TableGroup> findById(final Long id);

    List<TableGroup> findAll();
}
