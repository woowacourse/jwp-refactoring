package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        // given
        final int newOrderId = orderService.list().size() + 1;
        final Order order = createOrder(null, createOrderTable(5L, 1, null), List.of(1L, 2L));

        // when
        final Order actual = orderService.create(order);

        // then
        assertThat(actual.getId()).isEqualTo(newOrderId);
    }

    @DisplayName("주문 생성에 실패한다")
    @ParameterizedTest(name = "{0} 주문 생성 시 실패한다")
    @MethodSource("orderTableProvider")
    void create_Fail(
            final String name,
            final Long id,
            final List<Long> products
    ) {
        // given
        final Order order = createOrder(null, createOrderTable(id, null, null), products);

        // when
        assertThatThrownBy(() -> orderService.create(order));
    }

    private static Stream<Arguments> orderTableProvider() {
        return Stream.of(
                Arguments.of("상품이 없는", 5L, List.of()),
                Arguments.of("메뉴에 없는 상품", 5L, List.of(-1L)),
                Arguments.of("빈 테이블", 1L, List.of(1L, 2L)),
                Arguments.of("존재하지 않는", -1L, List.of(1L, 2L))
        );
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        // then
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = createOrder(1L, createOrderTable(3L, null, null), List.of());
        order.setOrderStatus("COMPLETION");

        // when
        final Order actual = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    private Order createOrder(
            final Long id,
            final OrderTable orderTable,
            final List<Long> products
    ) {
        orderTable.setEmpty(false);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Long product : products) {
            orderLineItems.add(createOrderLineItem(product));
        }

        final Order order = new Order();

        order.setId(id);
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    private static OrderLineItem createOrderLineItem(final Long value) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(value);
        orderLineItem.setQuantity(value);
        orderLineItem.setSeq(value);
        orderLineItem.setMenuId(value);
        return orderLineItem;
    }
}
