package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    void create_menu() {
        // given
        final Order forSaveOrder = new Order();
        forSaveOrder.setOrderTableId(1L);
        forSaveOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }},
                new OrderLineItem() {{
                    setMenuId(2L);
                    setQuantity(1);
                    setSeq(2L);
                }})
        );

        final Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderTableId(1L);
        savedOrder.setOrderedTime(LocalDateTime.now());
        savedOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }},
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(2L);
                    setQuantity(1);
                    setSeq(2L);
                }})
        );

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
        final Order wrongOrder = new Order();
        wrongOrder.setOrderLineItems(Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상품의 개수와 주문 내역의 개수가 다르면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_wrong_orderLineItem_count() {
        // given
        final Order wrongOrder = new Order();
        wrongOrder.setOrderTableId(1L);
        wrongOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }},
                new OrderLineItem() {{
                    setMenuId(2L);
                    setQuantity(1);
                    setSeq(2L);
                }})
        );

        given(menuDao.countByIdIn(any()))
                .willReturn(1L);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(wrongOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다.")
    @Test
    void create_order_fail_with_not_found_orderTable_() {
        // given
        final Order wrongOrder = new Order();
        wrongOrder.setOrderTableId(1L);
        wrongOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }},
                new OrderLineItem() {{
                    setMenuId(2L);
                    setQuantity(1);
                    setSeq(2L);
                }})
        );

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
    void create_order_fail_with_empty_orderTable_() {
        // given
        final Order wrongOrder = new Order();
        wrongOrder.setOrderTableId(1L);
        wrongOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }},
                new OrderLineItem() {{
                    setMenuId(2L);
                    setQuantity(1);
                    setSeq(2L);
                }})
        );

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
        final Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderStatus(OrderStatus.COOKING.name());
        order1.setOrderTableId(1L);
        order1.setOrderedTime(LocalDateTime.now());
        order1.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }}));

        final Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderStatus(OrderStatus.COOKING.name());
        order2.setOrderTableId(2L);
        order2.setOrderedTime(LocalDateTime.now());
        order2.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(2L);
                    setMenuId(2L);
                    setQuantity(4);
                    setSeq(2L);
                }}));

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
        final Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }}));

        final Order chagedOrder = new Order();
        chagedOrder.setId(1L);
        chagedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        chagedOrder.setOrderTableId(1L);
        chagedOrder.setOrderedTime(LocalDateTime.now());
        chagedOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }}));

        given(orderDao.findById(any()))
                .willReturn(Optional.of(order));

        // when
        final Order result = orderService.changeOrderStatus(order.getId(), chagedOrder);

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
        final Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }}));

        final Order chagedOrder = new Order();
        chagedOrder.setId(1L);
        chagedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        chagedOrder.setOrderTableId(1L);
        chagedOrder.setOrderedTime(LocalDateTime.now());
        chagedOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }}));

        given(orderDao.findById(any()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), chagedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 COMPLETION이면 주문 상태를 변경할 수 없다.")
    @Test
    void change_order_fail_with_completion_order() {
        // given
        final Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }}));

        final Order chagedOrder = new Order();
        chagedOrder.setId(1L);
        chagedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        chagedOrder.setOrderTableId(1L);
        chagedOrder.setOrderedTime(LocalDateTime.now());
        chagedOrder.setOrderLineItems(List.of(
                new OrderLineItem() {{
                    setOrderId(1L);
                    setMenuId(1L);
                    setQuantity(1);
                    setSeq(1L);
                }}));

        given(orderDao.findById(any()))
                .willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), chagedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
