package kitchenpos.ui;

import static fixture.MenuFixtures.존재하지_않는_메뉴_ID;
import static fixture.MenuFixtures.후라이드치킨_메뉴;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.common.IntegrationTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class OrderRestControllerTest {

    @Autowired
    private OrderRestController sut;

    @Autowired
    private TableRestController tableRestController;

    @DisplayName("주문 목록은 빈 목록일 수 없다.")
    @Test
    void createOrderByEmptyOrderLineItems() {
        // arrange
        changeOrderTableToExistStatus(1L);

        // act & assert
        assertThatThrownBy(() -> createOrder(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴로 주문을 생성할 수 없다.")
    @Test
    void createOrderByNotFoundMenu() {
        // arrange
        changeOrderTableToExistStatus(1L);

        // act & assert
        assertThatThrownBy(() -> createOrder(1L, createOrderLineItemRequest(존재하지_않는_메뉴_ID(), 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 메뉴로 주문을 생성할 수 없다.")
    @Test
    void createOrderByDuplicateMenus() {
        // arrange
        changeOrderTableToExistStatus(1L);

        // act & assert
        assertThatThrownBy(() ->
                createOrder(1L,
                        createOrderLineItemRequest(후라이드치킨_메뉴.id(), 1L),
                        createOrderLineItemRequest(후라이드치킨_메뉴.id(), 2L)
                )
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블에서 주문을 생성할 수 없다.")
    @Test
    void createOrderToEmptyTable() {
        // arrange
        changeOrderTableToEmptyStatus(1L);

        // act & assert
        assertThatThrownBy(() -> createOrder(1L, createOrderLineItemRequest(후라이드치킨_메뉴.id(), 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블에서 주문을 생성할 수 없다.")
    @Test
    void createOrderToNotFoundTable() {
        // arrange
        long notFoundOrderTableId = -1L;

        // act & assert
        assertThatThrownBy(() -> createOrder(notFoundOrderTableId, createOrderLineItemRequest(후라이드치킨_메뉴.id(), 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusToNotFoundOrder() {
        // arrange
        long notFoundOrderId = -1L;

        // act & assert
        assertThatThrownBy(() -> changeOrderStatus(notFoundOrderId, "MEAL"));
    }

    @DisplayName("계산이 완료된 주문의 상태를 변경할 수 없다.")
    @Test
    void changeCompletionOrderStatus() {
        // arrange
        changeOrderTableToExistStatus(1L);
        Order order = createOrder(1L, createOrderLineItemRequest(후라이드치킨_메뉴.id(), 1L));

        changeOrderStatus(order.getId(), "MEAL");
        changeOrderStatus(order.getId(), "COMPLETION");

        // act & assert
        assertThatThrownBy(() -> changeOrderStatus(order.getId(), "MEAL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem createOrderLineItemRequest(long menuId, long quantity) {
        OrderLineItem item = new OrderLineItem();
        item.setMenuId(menuId);
        item.setQuantity(quantity);
        return item;
    }

    private Order createOrder(long orderTableId, OrderLineItem... itemRequests) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(List.of(itemRequests));
        return sut.create(order).getBody();
    }

    private void changeOrderTableToEmptyStatus(long orderTableId) {
        OrderTable orderTableRequest = createOrderTableRequest(true);
        tableRestController.changeEmpty(orderTableId, orderTableRequest);
    }

    private void changeOrderTableToExistStatus(long orderTableId) {
        OrderTable orderTableRequest = createOrderTableRequest(false);
        tableRestController.changeEmpty(orderTableId, orderTableRequest);
    }

    private OrderTable createOrderTableRequest(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    private void changeOrderStatus(long orderId, String status) {
        Order order = new Order();
        order.setOrderStatus(status);
        sut.changeOrderStatus(orderId, order);
    }
}
