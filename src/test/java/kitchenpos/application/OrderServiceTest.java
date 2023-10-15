package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.FixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create_order() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, null, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, null, 2L, 2));
        final Order forSaveOrder = FixtureFactory.forSaveOrder(1L, orderLineItems);
        final List<OrderLineItem> savedorderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, 1L, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, 1L, 2L, 2));
        final Order savedOrder = FixtureFactory.savedOrder(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), savedorderLineItems);


        given(menuDao.countByIdIn(any()))
                .willReturn(2L);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(new OrderTable() {{
                    setId(1L);
                    setEmpty(false);
                }}));
        given(orderDao.save(forSaveOrder))
                .willReturn(savedOrder);

        // when
        final Order result = orderService.create(forSaveOrder);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(savedOrder.getId());
            softly.assertThat(result.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime());
            softly.assertThat(result.getOrderStatus()).isEqualTo(savedOrder.getOrderStatus());
            softly.assertThat(result.getOrderLineItems()).isEqualTo(savedOrder.getOrderLineItems());
            softly.assertThat(result.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId());
        });
    }

    @DisplayName("주문 상품이 없으면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_orderLineItem_empty() {
        // given
        final Order wrongOrder = FixtureFactory.forSaveOrder(1L, Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상품의 개수와 주문 내역의 개수가 다르면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_wrong_orderLineItem_count() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, null, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, null, 2L, 2));
        final Order wrongOrder = FixtureFactory.forSaveOrder(1L, orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn(1L);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_not_found_orderTable() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, null, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, null, 2L, 2));
        final Order wrongOrder = FixtureFactory.forSaveOrder(1L, orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn(2L);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_empty_orderTable() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, null, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, null, 2L, 2));
        final Order wrongOrder = FixtureFactory.forSaveOrder(1L, orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn(2L);

        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(new OrderTable() {{
                    setEmpty(true);
                }}));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문을 조회할 수 있다.")
    @Test
    void find_all_order() {
        // given
        final List<OrderLineItem> savedorderLineItems1 = List.of(
                FixtureFactory.savedOrderLineItem(1L, 1L, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, 1L, 2L, 2));
        final Order order1 = FixtureFactory.savedOrder(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), savedorderLineItems1);

        final List<OrderLineItem> savedorderLineItems2 = List.of(
                FixtureFactory.savedOrderLineItem(3L, 2L, 3L, 1),
                FixtureFactory.savedOrderLineItem(4L, 2L, 4L, 2));
        final Order order2 = FixtureFactory.savedOrder(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), savedorderLineItems2);

        final List<Order> orders = List.of(order1, order2);

        given(orderDao.findAll())
                .willReturn(orders);

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(orders);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change_order_status() {
        // given
        final List<OrderLineItem> savedOrderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, 1L, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, 1L, 2L, 2));
        final Order order = FixtureFactory.savedOrder(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), savedOrderLineItems);

        final Order changeOrder = FixtureFactory.savedOrder(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), savedOrderLineItems);

        given(orderDao.findById(any()))
                .willReturn(Optional.of(order));

        // when
        final Order result = orderService.changeOrderStatus(order.getId(), changeOrder);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(order.getId());
            softly.assertThat(result.getOrderedTime()).isEqualTo(order.getOrderedTime());
            softly.assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
            softly.assertThat(result.getOrderLineItems()).isEqualTo(order.getOrderLineItems());
            softly.assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTableId());
        });
    }

    @DisplayName("주문이 존재하지 않으면 주문 상태를 변경할 수 없다.")
    @Test
    void change_order_fail_with_not_found_order() {
        // given
        final Long wrongOrderId = 0L;
        final List<OrderLineItem> savedOrderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, 1L, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, 1L, 2L, 2));
        final Order changeOrder = FixtureFactory.savedOrder(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), savedOrderLineItems);

        given(orderDao.findById(any()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 COMPLETION이면 주문 상태를 변경할 수 없다.")
    @Test
    void change_order_fail_with_completion_order() {
        // given
        final List<OrderLineItem> savedOrderLineItems = List.of(
                FixtureFactory.savedOrderLineItem(1L, 1L, 1L, 1),
                FixtureFactory.savedOrderLineItem(2L, 1L, 2L, 2));
        final Order complitedOrder = FixtureFactory.savedOrder(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), savedOrderLineItems);

        final Order changeOrder = FixtureFactory.savedOrder(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), savedOrderLineItems);

        given(orderDao.findById(any()))
                .willReturn(Optional.of(complitedOrder));

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(complitedOrder.getId(), changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
