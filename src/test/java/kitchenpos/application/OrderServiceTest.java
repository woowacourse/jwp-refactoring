package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest{

    @Autowired
    private OrderService orderService;
    private Order order;

    @BeforeEach
    void setUp() {
        final OrderTable orderTable = getOrderTable(1L);
        orderTable.setId(1L);
        order = getOrder();
        order.setOrderLineItems(Arrays.asList(getOrderLineItem(order.getId())));
        final OrderLineItem orderLineItem = getOrderLineItem(order.getId());
        orderLineItem.setSeq(1L);

        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(order));
        given(orderDao.save(any())).willReturn(order);
    }

    @Test
    @DisplayName("주문을 하면 COOKING 상태로 바뀌어야 한다.")
    void changeStatus() {
        final Order savedOrder = orderService.create(order);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @Test
    @DisplayName("주문시간이 등록되어야 한다.")
    void createCurrentOrderDate() {
        final Order savedOrder = orderService.create(order);
        assertThat(savedOrder.getOrderedTime()).isNotNull();
    }

    @ParameterizedTest(name = "{1} 주문을 생성하면 예외가 발생한다.")
    @MethodSource("invalidParams")
    void createWithInvalidOrder(final Order order, final String testName) {
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(new Order(1L, null),
                        "아이템 목록이 비어있을 경우"),
                Arguments.of(new Order(1L, Arrays.asList(new OrderLineItem(), new OrderLineItem())),
                        "주문 상품 목록에 등록되지 않은 메뉴가 존재할 경우")
        );
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final Order savedOrder = orderService.create(order);
        final Order newStatusOrder = new Order();
        newStatusOrder.setOrderStatus(COMPLETION.name());

        // when
        final Order updateOrder = orderService.changeOrderStatus(savedOrder.getId(), newStatusOrder);

        // then
        assertThat(updateOrder.getOrderStatus()).isEqualTo(COMPLETION.name());
    }

    @Test
    @DisplayName("COMPLETION 상태에서는 주문 상태를 변경할 수 없다.")
    void changeInvalidOrderStatus() {
        // given
        final Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(COMPLETION.name());
        final Order newStatusOrder = new Order();
        newStatusOrder.setOrderStatus(COOKING.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(savedOrder));

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), newStatusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
