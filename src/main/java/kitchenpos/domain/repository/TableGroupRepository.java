package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;

import kitchenpos.domain.entity.TableGroup;

public interface TableGroupRepository {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
