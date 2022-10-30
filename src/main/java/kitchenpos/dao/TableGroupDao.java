package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.TableGroup;

public interface TableGroupDao {
    TableGroup save(TableGroup entity);

    TableGroup findById(Long id);

    List<TableGroup> findAll();
}
