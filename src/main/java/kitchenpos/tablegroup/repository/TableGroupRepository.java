package kitchenpos.tablegroup.repository;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;

public interface TableGroupRepository {
    TableGroup save(TableGroup entity);

    TableGroup findById(Long id);

    List<TableGroup> findAll();
}
