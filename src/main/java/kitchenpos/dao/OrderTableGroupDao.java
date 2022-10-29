package kitchenpos.dao;

import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.Optional;

public interface OrderTableGroupDao {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
