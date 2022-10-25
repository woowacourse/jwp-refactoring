package kitchenpos.ui;

import static fixture.MenuFixtures.후라이드치킨_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.IntegrationTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class TableRestControllerTest {

    @Autowired
    private TableRestController sut;

    @Autowired
    private OrderRestController orderRestController;

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @DisplayName("테이블의 상태 변경 시 테이블은 존재해야 한다.")
    @Test
    void tableMustExistWhenChangeEmptyStatus() {
        // arrange
        long notFoundTableId = -1L;

        // act & assert
        assertThatThrownBy(() -> changeOrderTableStatus(notFoundTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 상태 변경 시 테이블 그룹에 묶여 있으면 안된다.")
    @Test
    void tableMustNotIncludeToTableGroup() {
        // arrange
        groupTables(1L, 2L);

        // act & assert
        assertThatThrownBy(() -> changeOrderTableStatus(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 상태 변경 시 계산 완료여야 한다.")
    @Test
    void tableMustCompletion() {
        // arrange
        changeOrderTableStatus(1L, false);

        OrderLineItem item = createOrderLineItemRequest(후라이드치킨_메뉴.id(), 1L);
        createOrder(1L, item);

        // act & assert
        assertThatThrownBy(() -> changeOrderTableStatus(1L, true))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("방문 손님 수는 0명 이상이어야 한다.")
    @Test
    void numberOfGuestMustOverZero() {
        assertThatThrownBy(() -> changeNumberOfGuest(1L, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 방문 손님 수는 변경할 수 없다.")
    @Test
    void changeNumberOfGuestToEmptyTable() {
        assertThatThrownBy(() -> changeNumberOfGuest(1L, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블의 방문 손님 수는 변경할 수 없다.")
    @Test
    void changeNumberOfGuestToNotFoundTable() {
        long notFoundTableId = -1L;

        assertThatThrownBy(() -> changeNumberOfGuest(notFoundTableId, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void groupTables(long... tableIds) {
        List<OrderTable> orderTables = Arrays.stream(tableIds)
                .mapToObj(id -> {
                    OrderTable orderTable = new OrderTable();
                    orderTable.setId(id);
                    return orderTable;
                })
                .collect(Collectors.toList());
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroupRestController.create(tableGroup);
    }

    private void changeOrderTableStatus(long orderTableId, boolean isEmpty) {
        OrderTable orderTableRequest = createOrderTableRequest(isEmpty);
        sut.changeEmpty(orderTableId, orderTableRequest);
    }

    private OrderTable createOrderTableRequest(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    private OrderLineItem createOrderLineItemRequest(long menuId, long quantity) {
        OrderLineItem item = new OrderLineItem();
        item.setMenuId(menuId);
        item.setQuantity(quantity);
        return item;
    }

    private void createOrder(long orderTableId, OrderLineItem... itemRequests) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(List.of(itemRequests));
        orderRestController.create(order);
    }

    private void changeNumberOfGuest(long orderTableId, int numberOfGuest) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuest);
        sut.changeNumberOfGuests(orderTableId, orderTable);
    }
}
