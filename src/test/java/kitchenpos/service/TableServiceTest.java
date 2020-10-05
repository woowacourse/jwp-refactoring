package kitchenpos.service;

import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createOrder;
import static kitchenpos.utils.TestObjectUtils.createOrderLineItem;
import static kitchenpos.utils.TestObjectUtils.createOrderTable;
import static kitchenpos.utils.TestObjectUtils.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문 테이블 생성 - 성공")
    @Test
    void create_SuccessToCreate() {
        OrderTable orderTable = createOrderTable(0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertAll(() -> {
            assertThat(savedOrderTable.getId()).isNotNull();
            assertThat(savedOrderTable.getTableGroupId()).isNull();
            assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0);
            assertThat(savedOrderTable.isEmpty()).isTrue();
        });
    }

    @DisplayName("주문 테이블 전체 조회 - 성공")
    @Test
    void list_SuccessToFindAll() {
        List<OrderTable> list = tableService.list();

        assertThat(list).isNotEmpty();
    }

    @DisplayName("주문 테이블 비어있는 상태 변경 - 성공")
    @Test
    void changeEmpty_SuccessToChange() {
        OrderTable orderTable = createOrderTable(0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setEmpty(false);
        OrderTable changedOrderTable = tableService
            .changeEmpty(savedOrderTable.getId(), savedOrderTable);

        assertAll(() -> {
            assertThat(changedOrderTable.getId()).isNotNull();
            assertThat(changedOrderTable.getTableGroupId()).isNull();
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(0);
            assertThat(changedOrderTable.isEmpty()).isFalse();
        });
    }

    @DisplayName("주문 테이블 비어있는 상태 변경 - 예외, 주문 테이블을 찾지 못하는 경우")
    @Test
    void changeEmpty_NotFoundOrderTable_ThrownException() {
        OrderTable orderTable = createOrderTable(0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setId(NOT_EXIST_VALUE);
        savedOrderTable.setEmpty(false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비어있는 상태 변경 - 예외, 주문 테이블이 그룹핑 되어있는 경우")
    @Test
    void changeEmpty_OrderTableIsGroup_ThrownException() {
        OrderTable orderTable = createOrderTable(0, true);
        OrderTable orderTable2 = createOrderTable(0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable savedOrderTable2 = tableService.create(orderTable2);

        tableGroupService
            .create(createTableGroup(Arrays.asList(savedOrderTable, savedOrderTable2)));
        savedOrderTable.setEmpty(false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비어있는 상태 변경 - 예외, 주문 테이블의 주문 상태가 COOKING인 경우")
    @Test
    void changeEmpty_OrderStatusOfOrderTableIsCooking_ThrownException() {
        OrderTable orderTable = createOrderTable(1, false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        List<OrderLineItem> orderLineItems = Collections.singletonList(createOrderLineItem(1L, 1L));
        orderService.create(createOrder(savedOrderTable.getId(), orderLineItems));
        savedOrderTable.setEmpty(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비어있는 상태 변경 - 예외, 주문 테이블의 주문 상태가 MEAL인 경우")
    @Test
    void changeEmpty_OrderStatusOfOrderTableIsMeal_ThrownException() {
        OrderTable orderTable = createOrderTable(1, false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        List<OrderLineItem> orderLineItems = Collections.singletonList(createOrderLineItem(1L, 1L));
        Order order = orderService.create(createOrder(savedOrderTable.getId(), orderLineItems));
        savedOrderTable.setEmpty(true);
        order.setOrderStatus(MEAL.name());

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 게스트 수 변경 - 성공")
    @Test
    void changeNumberOfGuests_SuccessToChange() {
        OrderTable orderTable = createOrderTable(1, false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setNumberOfGuests(4);

        OrderTable changedOrderTable = tableService
            .changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

        assertAll(() -> {
            assertThat(changedOrderTable.getId()).isNotNull();
            assertThat(changedOrderTable.getTableGroupId()).isNull();
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
            assertThat(changedOrderTable.isEmpty()).isFalse();
        });
    }

    @DisplayName("주문 테이블 게스트 수 변경 - 예외, 게스트 수가 0보다 작은 경우")
    @Test
    void changeNumberOfGuests_NumberOfGuestsLessThanZero_ThrownException() {
        OrderTable orderTable = createOrderTable(1, false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setNumberOfGuests(-1);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 게스트 수 변경 - 예외, 주문 테이블을 찾지 못하는 경우")
    @Test
    void changeNumberOfGuests_NotFoundOrderTable_ThrownException() {
        OrderTable orderTable = createOrderTable(1, false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setId(NOT_EXIST_VALUE);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 게스트 수 변경 - 예외, 주문 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuests_OrderTableIsEmpty_ThrownException() {
        OrderTable orderTable = createOrderTable(0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setNumberOfGuests(4);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }


}
