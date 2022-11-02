package kitchenpos.order.dao;

import java.util.List;
import java.util.Optional;

import kitchenpos.order.domain.TableGroup;

public interface TableGroupDao {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    TableGroup getById(Long id);

    List<TableGroup> findAll();
}
