package kitchenpos.integration.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceIntegrationTest extends IntegrationTest {

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(orderTable);
    }

    /**
     * 여기도 테스트 격리가 잘 안됨
     */
    @DisplayName("테이블의 목록을 조회할 수 있다.")
    @Test
    void list_Valid_Success() {
        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).isNotEmpty();
    }

    @DisplayName("테이블 상태를 변경할 수 있다.")
    @Test
    void changeEmpty_Valid_Success() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(0);
        targetOrderTable.setEmpty(true);

        // when
        OrderTable changedOrderTable = tableService
            .changeEmpty(savedOrderTable.getId(), targetOrderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 상태는 주문 테이블이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeEmpty_NonExistingOrderTable_Fail() {
        // given
        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(0);
        targetOrderTable.setEmpty(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(100L, targetOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태는 주문 테이블이 단체 지정되어 있으면 변경할 수 없다.")
    @Test
    void changeEmpty_GroupingOrderTable_Fail() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(true);

        OrderTable savedOrderTable1 = tableService.create(orderTable1);
        OrderTable savedOrderTable2 = tableService.create(orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable1, savedOrderTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        savedOrderTable1.setTableGroupId(savedTableGroup.getId());

        // when
        // then
        assertThatThrownBy(
            () -> tableService.changeEmpty(savedOrderTable1.getId(), savedOrderTable2)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태는 주문 상태가 `조리` 또는 `식사`면 변경할 수 없다.")
    @Test
    void changeEmpty_InvalidOrderStatus_Fail() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(2L);
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        orderService.create(order);

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(0);
        targetOrderTable.setEmpty(true);

        // when
        // then
        assertThatThrownBy(
            () -> tableService.changeEmpty(savedOrderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests_Valid_Success() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(6);
        orderTable.setEmpty(false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(0);
        targetOrderTable.setEmpty(true);

        // when
        OrderTable changedOrderTable = tableService
            .changeNumberOfGuests(savedOrderTable.getId(), targetOrderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isZero();
    }

    @DisplayName("테이블에 방문한 손님수가 올바르지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_Negative_Fail() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(6);
        orderTable.setEmpty(false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(-4);
        targetOrderTable.setEmpty(true);

        // when
        // then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님수는 주문 테이블이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_NonExistingOrderTable_Fail() {
        // given
        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(0);
        targetOrderTable.setEmpty(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, targetOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님수는 주문 테이블이 비어 있으면(손님이 없으면) 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_EmptyOrderTable_Fail() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setNumberOfGuests(0);
        targetOrderTable.setEmpty(true);

        // when
        // then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
