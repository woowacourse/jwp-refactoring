package kitchenpos.table.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.TableGroup;

public interface TableGroupDao {

    TableGroupDto save(TableGroup entity);

    Optional<TableGroupDto> findById(Long id);

    List<TableGroupDto> findAll();
}
