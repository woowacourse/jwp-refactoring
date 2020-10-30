package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import kitchenpos.domain.Table;

public interface TableDao {
    Table save(Table entity);

    Optional<Table> findById(Long id);

    List<Table> findAll();

    List<Table> findAllByIdIn(List<Long> ids);

    List<Table> findAllByTableGroupId(Long tableGroupId);
}
