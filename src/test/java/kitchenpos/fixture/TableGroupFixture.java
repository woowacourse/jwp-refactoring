package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.InMemoryTableGroupDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static final Long 주문상태_안료되지_않은_첫번째테이블그룹 = 1L;
    public static final Long 주문상태_완료된_두번째테이블그룹 = 2L;
    public static final Long 세번째테이블그룹 = 3L;

    private final TableGroupDao tableGroupDao;
    private List<TableGroup> fixtures;

    public TableGroupFixture(final TableGroupDao tableGroupDao) {
        this.tableGroupDao = tableGroupDao;
    }

    public static TableGroupFixture setUp() {
        final TableGroupFixture tableGroupFixture = new TableGroupFixture(new InMemoryTableGroupDao());
        tableGroupFixture.fixtures = tableGroupFixture.createTableGroups();
        return tableGroupFixture;
    }

    public static TableGroup createTableGroup(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    private List<TableGroup> createTableGroups() {
        return List.of(
                saveTableGroup(),
                saveTableGroup(),
                saveTableGroup(),
                saveTableGroup(),
                saveTableGroup()
        );
    }

    private TableGroup saveTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroupDao.save(tableGroup);
    }

    public TableGroupDao getTableGroupDao() {
        return tableGroupDao;
    }

    public List<TableGroup> getFixtures() {
        return fixtures;
    }
}
