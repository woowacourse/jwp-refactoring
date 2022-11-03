package kitchenpos.order.repository.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.TableGroup;

public interface TableGroupDao {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
