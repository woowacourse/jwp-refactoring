package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.TableGroup;

public interface TableGroupRepository {
    List<TableGroup> findAll();

    TableGroup save(TableGroup entity);
}
