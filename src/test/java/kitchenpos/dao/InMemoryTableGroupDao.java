package kitchenpos.dao;

import java.util.function.BiConsumer;
import kitchenpos.domain.TableGroup;

public class InMemoryTableGroupDao extends InMemoryAbstractDao<TableGroup> implements TableGroupDao {

    @Override
    protected BiConsumer<TableGroup, Long> setId() {
        return TableGroup::setId;
    }
}
