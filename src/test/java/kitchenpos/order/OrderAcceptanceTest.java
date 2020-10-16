package kitchenpos.order;

import static java.util.Collections.*;
import static kitchenpos.ui.OrderRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderAcceptanceTest extends AcceptanceTest {
    /**
     * 주문을 관리한다.
     * <p>
     * When 주문 생성 요청.
     * Then 주문이 생성 된다.
     * <p>
     * Given 주문이 생성 되어 있다.
     * When 주문 전체 조회 요청.
     * Then 전체 주문을 반환한다.
     * <p>
     * When 주문 상태 변경 요청.
     * Then 변경된 주문 정보를 반환한다.
     */
    @DisplayName("주문 관리")
    @TestFactory
    Stream<DynamicTest> manageOrder() throws Exception {
        // 주문 생성
        Long orderId = createOrder();
        assertThat(orderId).isNotNull();

        return Stream.of(
                dynamicTest("주문 전체 조회", () -> {
                    List<Order> orders = getAll(Order.class, API_ORDERS);
                    Order lastOrder = getLastItem(orders);

                    assertThat(lastOrder.getId()).isEqualTo(orderId);
                }),
                dynamicTest("주문 상태 변경", () -> {
                    Order request = new Order();
                    String orderStatus = OrderStatus.MEAL.name();
                    request.setOrderStatus(orderStatus);
                    Order order = changeOrderStatus(request, orderId);

                    assertAll(
                            () -> assertThat(order.getId()).isEqualTo(orderId),
                            () -> assertThat(order.getOrderStatus()).isEqualTo(orderStatus)
                    );
                })
        );
    }

    private Long createOrder() throws Exception {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);
        Order order = new Order();
        Long orderTableId = 1L;
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(singletonList(orderLineItem));

        changeOrderTableEmpty(false, orderTableId);

        String request = objectMapper.writeValueAsString(order);
        return post(request, API_ORDERS);
    }

    private Order changeOrderStatus(Order order, Long orderId) throws Exception {
        String request = objectMapper.writeValueAsString(order);
        return put(Order.class, request, API_ORDERS + "/" + orderId + "/order-status");
    }
}
