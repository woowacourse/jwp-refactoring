package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;

public interface TableGroupDao {

    Long save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
