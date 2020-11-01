package kitchenpos.table.domain;

import java.util.List;
import java.util.Optional;

public interface TableDao {
    Table save(Table entity);

    Optional<Table> findById(Long id);

    List<Table> findAll();

    List<Table> findAllByIdIn(List<Long> ids);

    List<Table> findAllByTableGroupId(Long tableGroupId);
}
