package kitchenpos.ui;

import static fixture.MenuFixtures.후라이드치킨_메뉴;
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
class TableGroupRestControllerTest {

    @Autowired
    private TableGroupRestController sut;

    @Autowired
    private TableRestController tableRestController;

    @Autowired
    private OrderRestController orderRestController;

    @DisplayName("그룹핑을 원하는 테이블은 2개 이상이어야 한다.")
    @Test
    void groupingOrderTableSizeMustOverTwo() {
        // arrange
        changeOrderTableStatus(1L, true);

        // act & assert
        assertThatThrownBy(() -> groupOrderTables(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹핑을 원하는 테이블은 존재해야 한다.")
    @Test
    void groupingOrderTableMustExist() {
        // arrange
        changeOrderTableStatus(1L, true);
        changeOrderTableStatus(2L, true);

        // act & assert
        long notFoundOrderTableId = -1L;
        assertThatThrownBy(() -> groupOrderTables(notFoundOrderTableId, 1L, 2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹핑을 원하는 테이블은 중복될 수 없다.")
    @Test
    void groupingOrderTableMustDistinct() {
        // arrange
        changeOrderTableStatus(1L, true);
        changeOrderTableStatus(2L, true);

        // act & assert
        assertThatThrownBy(() -> groupOrderTables(1L, 2L, 2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹핑을 원하는 테이블은 빈 테이블이어야 한다.")
    @Test
    void groupingOrderTableMustEmpty() {
        // arrange
        changeOrderTableStatus(1L, false);
        changeOrderTableStatus(2L, true);

        // act & assert
        assertThatThrownBy(() -> groupOrderTables(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹핑을 원하는 테이블은 기존 그룹이 없어야 한다.")
    @Test
    void groupingOrderTableMustNotHasGroup() {
        // arrange
        changeOrderTableStatus(1L, true);
        changeOrderTableStatus(2L, true);
        groupOrderTables(1L, 2L);

        changeOrderTableStatus(3L, true);

        // act & assert
        assertThatThrownBy(() -> groupOrderTables(1L, 3L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 해제를 원하는 테이블은 계산 완료여야 한다.")
    @Test
    void ungroupingOrderTablesMustCompletion() {
        // arrange
        changeOrderTableStatus(1L, true);
        changeOrderTableStatus(2L, true);
        TableGroup tableGroup = groupOrderTables(1L, 2L);

        createOrder(1L, createOrderLineItemRequest(후라이드치킨_메뉴.id(), 1L));

        // act
        assertThatThrownBy(() -> sut.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void changeOrderTableStatus(long orderTableId, boolean isEmpty) {
        OrderTable orderTableRequest = createOrderTableRequest(isEmpty);
        tableRestController.changeEmpty(orderTableId, orderTableRequest);
    }

    private OrderTable createOrderTableRequest(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    private TableGroup groupOrderTables(long... tableIds) {
        List<OrderTable> orderTables = Arrays.stream(tableIds)
                .boxed()
                .map(id -> {
                    OrderTable orderTable = new OrderTable();
                    orderTable.setId(id);
                    return orderTable;
                })
                .collect(Collectors.toList());
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return sut.create(tableGroup).getBody();
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

}
