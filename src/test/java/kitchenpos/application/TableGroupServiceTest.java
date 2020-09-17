package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    private TableGroup tableGroup;

    @Autowired
    private OrderTableDao tableDao;
    private OrderTable table1;
    private OrderTable table2;

    @BeforeEach
    void setUp() {
        OrderTable table = OrderTable.builder()
            .empty(true)
            .build();
        table1 = tableDao.save(table);
        table2 = tableDao.save(table);

        tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1, table2))
            .createdDate(LocalDateTime.now())
            .build();
    }

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() {
        TableGroup create = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(create.getId()).isNotNull(),
            () -> assertThat(create.getOrderTables().get(0).isEmpty()).isFalse(),
            () -> assertThat(create.getOrderTables().get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 2개 미만의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_LessTable() {
        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 저장되지 않은 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable notExistTable = OrderTable.builder()
            .id(100L)
            .empty(true)
            .build();

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(notExistTable))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블이 아닌 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotEmptyTable() {
        OrderTable emptyTable = OrderTable.builder()
            .empty(false)
            .build();
        emptyTable = tableDao.save(emptyTable);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(emptyTable))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 다른 그룹의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_OthersTable() {
        OrderTable othersTable = OrderTable.builder()
            .empty(true)
            .tableGroupId(tableGroup.getId())
            .build();
        othersTable = tableDao.save(othersTable);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(othersTable))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }
}