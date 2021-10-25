package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("주문 서비스 테스트")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        Order created = orderService.create(order());

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(created.getOrderLineItems()).isNotEmpty()
        );
    }

    @DisplayName("주문 리스트를 불러온다.")
    @Test
    void list() {
        orderService.create(order());
        List<Order> orders = orderService.list();
        assertThat(orders.size()).isEqualTo(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Order order = orderService.create(order());
        Order changedOrderRequest = order.changeStatus(OrderStatus.COMPLETION);
        Order changedOrder = orderService.changeOrderStatus(order.getId(), changedOrderRequest);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}