package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.dto.OrderTableRequest;

@SpringBootTest
@Transactional
class OrderServiceTest {

    private static final long NOT_EXIST_ID = 999L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableService orderTableService;

    private Long orderTableId;

    @BeforeEach
    void init() {
        orderTableId = orderTableService.create(new OrderTableRequest(1, false)).getId();
    }

    @Test
    @DisplayName("존재하지 않는 menu가 orderItems에 있을 경우 예외를 발생시킨다.")
    void createWithNotExistOrderItemError() {
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest(NOT_EXIST_ID, 1)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 orderTable이 있을 경우 예외를 발생시킨다.")
    void createWithNotExistOrderTableError() {
        //when
        OrderRequest orderRequest = new OrderRequest(NOT_EXIST_ID, Arrays.asList(new OrderLineItemRequest(1L, 1)));

        //then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("orderTable이 비어있을 경우 예외를 발생시킨다.")
    void createWithEmptyOrderTableError() {
        //when
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest(1L, 1)));

        //then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 주문 목록을 조회한다.")
    void findList() {
        //given
        List<Order> orders = orderService.list();

        //when
        OrderRequest orderRequest = new OrderRequest(orderTableId, Arrays.asList(new OrderLineItemRequest(1L, 1)));
        orderService.create(orderRequest);

        //then
        assertThat(orderService.list()).hasSize(orders.size() + 1);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        //given
        OrderRequest orderRequest = new OrderRequest(orderTableId, Arrays.asList(new OrderLineItemRequest(1L, 1)));
        Long orderId = orderService.create(orderRequest).getId();

        //when
        Order actual = orderService.changeOrderStatus(orderId, new OrderStatusRequest("MEAL"));

        //then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("존재하지 않는 주문일 경우 예외를 발생시킨다.")
    void changeOrderStatusNotExistOrderError() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_ID, new OrderStatusRequest("MEAL")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("COMPLETION 상태의 주문일 경우 예외를 발생시킨다.")
    void changeOrderStatusCompletionError() {
        //given
        OrderRequest orderRequest = new OrderRequest(orderTableId, Arrays.asList(new OrderLineItemRequest(1L, 1)));
        Long orderId = orderService.create(orderRequest).getId();

        //when
        Order actual = orderService.changeOrderStatus(orderId, new OrderStatusRequest("COMPLETION"));

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new OrderStatusRequest("MEAL")))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
