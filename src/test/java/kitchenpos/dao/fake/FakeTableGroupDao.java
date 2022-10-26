package kitchenpos.dao.fake;

import static kitchenpos.application.fixture.TableGroupFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class FakeTableGroupDao implements TableGroupDao {

    private static final Map<Long, TableGroup> stores = new HashMap<>();
    private static Long id = 0L;

    @Override
    public TableGroup save(final TableGroup entity) {
        TableGroup tableGroup = generateTableGroup(++id, entity);
        stores.put(id, tableGroup);
        return tableGroup;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return Optional.of(stores.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(stores.values());
    }

    public static void deleteAll() {
        stores.clear();
    }
}
