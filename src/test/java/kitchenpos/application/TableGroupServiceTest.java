package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1 = tableService.create(orderTable1);

        orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2 = tableService.create(orderTable2);

        orderTable3 = new OrderTable();
        orderTable3.setEmpty(false);
        orderTable3.setNumberOfGuests(4);
        orderTable3 = tableService.create(orderTable3);

        tableGroup = new TableGroup();
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다")
    void create() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        tableGroup.setOrderTables(orderTables);

        final TableGroup createTableGroup = tableGroupService.create(tableGroup);

        assertThat(createTableGroup.getOrderTables())
                .hasSize(2);
    }

    @Test
    @DisplayName("테이블 개수가 1개 이하일 때 그룹을 생성하면 예외를 반환한다")
    void create_oneTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 그룹을 생성하면 예외를 반환한다")
    void create_notEmptyTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable3);
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        tableGroup.setOrderTables(orderTables);

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
