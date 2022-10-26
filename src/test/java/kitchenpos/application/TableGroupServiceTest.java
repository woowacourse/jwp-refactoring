package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql("/schema.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;

    @BeforeEach
    void setUp() {
        orderTable1 = tableService.create(0, true);
        orderTable2 = tableService.create(0, true);
        orderTable3 = tableService.create(4, false);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다")
    void create() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        final TableGroup createTableGroup = tableGroupService.create(orderTables);

        assertThat(createTableGroup.getOrderTables())
                .hasSize(2);
    }

    @Test
    @DisplayName("테이블 개수가 1개 이하일 때 그룹을 생성하면 예외를 반환한다")
    void create_oneTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);

        assertThatThrownBy(() -> tableGroupService.create(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 그룹을 생성하면 예외를 반환한다")
    void create_notEmptyTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable3);

        assertThatThrownBy(() -> tableGroupService.create(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        final TableGroup tableGroup = tableGroupService.create(orderTables);

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
