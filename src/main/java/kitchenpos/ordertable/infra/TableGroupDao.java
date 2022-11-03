package kitchenpos.ordertable.infra;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.domain.TableGroup;

public interface TableGroupDao {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
