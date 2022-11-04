package kitchenpos.tablegroup.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.tablegroup.domain.TableGroup;

public interface TableGroupRepository {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
