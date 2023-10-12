package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
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

    private List<OrderLineItem> orderLineItems;
    private Order order;
    private List<Long> menuIds;
    private OrderTable orderTable;

    @BeforeEach
    void beforeEach() {
        orderLineItems = List.of(new OrderLineItem(), new OrderLineItem());

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);

        order = new Order();
        order.setId(1L);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(orderLineItems);

        menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        //given
        order.setOrderLineItems(orderLineItems);
        given(menuDao.countByIdIn(menuIds))
                .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.save(order))
                .willReturn(order);
        for (final OrderLineItem orderLineItem : orderLineItems) {
            given(orderLineItemDao.save(orderLineItem))
                    .willReturn(orderLineItem);
        }

        //when
        final Order actual = orderService.create(order);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual).isEqualTo(order);
        });
    }

    @DisplayName("주문 생성 시 orderLineItems이 비어있으면 예외를 던진다.")
    @Test
    void orderLineItems이_비어있으면_예외() {
        //given
        order.setOrderLineItems(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 orderTable이 empty면 예외를 던진다.")
    @Test
    void orderTable이_empty면_예외() {
        //given
        order.setOrderLineItems(orderLineItems);
        orderTable.setEmpty(true);
        order.setOrderTableId(orderTable.getId());
        given(menuDao.countByIdIn(menuIds))
                .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order를 전체 조회한다.")
    @Test
    void order_전체_조회() {
        //given
        given(orderDao.findAll())
                .willReturn(List.of(order));
        given(orderLineItemDao.findAllByOrderId(order.getId()))
                .willReturn(orderLineItems);

        //when
        final List<Order> actual = orderService.list();

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.size()).isEqualTo(1);
            softly.assertThat(actual.get(0).getId()).isEqualTo(order.getId());
        });
    }

    @DisplayName("order status를 변경한다.")
    @Test
    void order_status_변경() {
        //given
        final Order expected = order;
        given(orderDao.findById(expected.getId()))
                .willReturn(Optional.of(expected));
        given(orderDao.save(expected))
                .willReturn(expected);
        given(orderLineItemDao.findAllByOrderId(expected.getId()))
                .willReturn(orderLineItems);

        //when
        final Order actual = orderService.changeOrderStatus(order.getId(), expected);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
            softly.assertThat(actual.getOrderLineItems().size()).isEqualTo(expected.getOrderLineItems().size());
            softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
            softly.assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
        });
    }

    @DisplayName("order status가 complete면 변경할 수 없다.")
    @Test
    void order_status가_complete면_예외() {
        //given
        final Order parameter = new Order();
        parameter.setOrderStatus(OrderStatus.MEAL.name());
        parameter.setOrderLineItems(orderLineItems);

        given(orderDao.findById(order.getId()))
                .willReturn(Optional.of(order));
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), parameter))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
