package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dao.TestTableGroupDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static final Long 첫번째테이블그룹 = 1L;
    public static final Long 두번째테이블그룹 = 2L;
    public static final Long 세번째테이블그룹 = 3L;
    public static final Long 네번째테이블그룹 = 4L;

    private final TableGroupDao tableGroupDao;
    private List<TableGroup> fixtures;

    private TableGroupFixture(TableGroupDao tableGroupDao) {
        this.tableGroupDao = tableGroupDao;
    }

    public static TableGroupFixture createFixture() {
        TableGroupFixture tableGroupFixture = new TableGroupFixture(new TestTableGroupDao());
        tableGroupFixture.fixtures = tableGroupFixture.createTableGroups();
        return tableGroupFixture;
    }

    private List<TableGroup> createTableGroups() {
        return Arrays.asList(
            saveTableGroup(),
            saveTableGroup(),
            saveTableGroup(),
            saveTableGroup(),
            saveTableGroup(),
            saveTableGroup(),
            saveTableGroup(),
            saveTableGroup()
        );
    }

    private TableGroup saveTableGroup() {
        TableGroup tableGroup = new TableGroup();
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
