package kitchenpos.application.dao;

import java.util.Comparator;
import java.util.function.BiConsumer;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class TestTableGroupDao extends TestAbstractDao<TableGroup> implements TableGroupDao {

    @Override
    protected BiConsumer<TableGroup, Long> setIdConsumer() {
        return TableGroup::setId;
    }

    @Override
    protected Comparator<TableGroup> comparatorForSort() {
        return Comparator.comparingLong(TableGroup::getId);
    }
}
