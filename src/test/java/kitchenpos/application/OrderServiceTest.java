package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.application.request.OrderChangeStatusRequest;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.application.request.OrderRequest.OrderLineItemRequest;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void create() {
        OrderTable orderTable = OrderTable.of(1L, null, 1, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L,  10);
        OrderRequest request = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItem));

        OrderResponse response = orderService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void createThrowExceptionWhenNotCollectOrderLineItems() {
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(0L,  10);
        OrderRequest request = new OrderRequest(1L, List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴가 포함되어 있습니다.");
    }

    @Test
    void createThrowExceptionWhenEmptyOrderTable() {
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L,  10);
        OrderRequest request = new OrderRequest(0L, List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    void createThrowExceptionWhenEmptyTable() {
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L,  10);
        OrderRequest request = new OrderRequest(1L, List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있습니다.");
    }

    @Test
    void list() {
        List<OrderResponse> response = orderService.list();

        assertThat(response.size()).isEqualTo(0);
    }

    @Test
    void changeOrderStatus() {
        Order order = Order.of(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        Long savedId = orderDao.save(order)
                .getId();
        OrderChangeStatusRequest request = new OrderChangeStatusRequest("MEAL");

        OrderResponse response = orderService.changeOrderStatus(savedId, request);

        assertThat(response.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }

    @Test
    void changeOrderStatusThrowExceptionWhenNotExistOrder() {
        OrderChangeStatusRequest request = new OrderChangeStatusRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("order가 존재하지 않습니다.");
    }

    @Test
    void changeOrderStatusThrowExceptionWhenAlreadyCompleteOrder() {
        Order order = Order.of(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), new ArrayList<>());
        Long savedId = orderDao.save(order)
                .getId();
        OrderChangeStatusRequest request = new OrderChangeStatusRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 이미 완료되었습니다.");
    }
}
