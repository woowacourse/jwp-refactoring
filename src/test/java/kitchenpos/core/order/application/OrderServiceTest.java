package kitchenpos.core.order.application;

import static kitchenpos.core.order.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.OrderFixture.getOrder;
import static kitchenpos.fixture.OrderFixture.getOrderLineItem;
import static kitchenpos.fixture.OrderFixture.getOrderLineItemRequest;
import static kitchenpos.fixture.OrderFixture.getOrderRequest;
import static kitchenpos.fixture.TableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.common.ServiceTest;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;
    private Order order;

    @BeforeEach
    void setUp() {
        final OrderTable orderTable = getOrderTable();
        order = getOrder();
        order.changeOrderLineItems(Arrays.asList(getOrderLineItem(order.getId())));
        final OrderLineItem orderLineItem = getOrderLineItem(order.getId());

        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(order));
        given(orderDao.save(any())).willReturn(order);
    }

    @Test
    @DisplayName("주문을 생성하면 주문 상품 목록들의 orderId가 할당되어야 한다.")
    void createWithOrderLineItems() {
        // given
        final OrderTable orderTable = getOrderTable(false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // when
        order = getOrderRequest(1L, Arrays.asList(getOrderLineItemRequest()));
        final Order savedOrder = orderService.create(this.order);

        // then
        final List<OrderLineItem> orderLineItems = savedOrder.getOrderLineItems();
        assertThat(orderLineItems.get(0).getOrderId()).isEqualTo(savedOrder.getId());
    }

    @ParameterizedTest(name = "{1} 주문을 생성하면 예외가 발생한다.")
    @MethodSource("invalidParams")
    void createWithInvalidOrder(final Order order, final String testName) {
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(Order.of(1L, Arrays.asList(getOrderLineItemRequest(), getOrderLineItemRequest()), order -> {}),
                        "주문 상품 목록에 등록되지 않은 메뉴가 존재할 경우")
        );
    }

    @Test
    @DisplayName("비어있는 주문 테이블로 주문을 생성할 경우 예외가 발생한다.")
    void createWithEmptyOrderTable() {
        // given
        final OrderTable orderTable = getOrderTable(true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // when
        order = getOrderRequest(1L);

        // when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비워져있으면 주문을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final Order savedOrder = orderService.create(order);
        final Order newStatusOrder = getOrderRequest(MEAL.name());

        // when
        final Order updateOrder = orderService.changeOrderStatus(savedOrder.getId(), newStatusOrder);

        // then
        assertThat(updateOrder.getOrderStatus()).isEqualTo(MEAL);
    }
}
