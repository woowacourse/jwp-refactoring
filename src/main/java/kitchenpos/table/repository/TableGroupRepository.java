package kitchenpos.table.repository;

import java.util.List;
import kitchenpos.table.domain.TableGroup;

public interface TableGroupRepository {
    TableGroup save(TableGroup entity);

    TableGroup findById(Long id);

    List<TableGroup> findAll();
}
