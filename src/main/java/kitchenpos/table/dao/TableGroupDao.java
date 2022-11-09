package kitchenpos.table.dao;

import java.util.List;
import java.util.Optional;

import kitchenpos.table.domain.TableGroup;

public interface TableGroupDao {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    TableGroup getById(Long id);

    List<TableGroup> findAll();
}
