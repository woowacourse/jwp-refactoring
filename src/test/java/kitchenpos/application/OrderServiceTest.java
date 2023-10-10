package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private MenuDao menuDao;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private OrderLineItemDao orderLineItemDao;

    @Test
    void 주문을_생성한다() {
        // given
        Order order = new Order();
        order.setId(1L);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        order.setOrderTableId(orderTable.getId());

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds))
                .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.save(order))
                .willReturn(order);
        given(orderLineItemDao.save(orderLineItem))
                .willReturn(orderLineItem);

        // when
        Order result = orderService.create(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            softly.assertThat(result.getOrderTableId()).isEqualTo(orderTable.getId());
            softly.assertThat(result.getOrderLineItems()).containsExactly(orderLineItem);
            softly.assertThat(result.getOrderedTime()).isNotNull();
        });
    }

    @Test
    void 주문을_전체_조회한다() {
        // given
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> expected = List.of(order1, order2);

        given(orderDao.findAll())
                .willReturn(expected);

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).containsExactly(order1, order2);
    }

    @ParameterizedTest(name = "주문 상태를 {0}에서 {1}로 변경한다")
    @CsvSource(value = {"COOKING:MEAL", "COOKING:COMPLETION", "MEAL:COMPLETION", "COOKING:COOKING", "MEAL:MEAL"}, delimiter = ':')
    void 주문_상태를_변경한다(OrderStatus fromStatus, OrderStatus toStatus) {
        // given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(fromStatus.name());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        Order changeOrder = new Order();
        changeOrder.setOrderStatus(toStatus.name());

        given(orderDao.findById(order.getId()))
                .willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(order.getId()))
                .willReturn(orderLineItems);

        // when
        Order result = orderService.changeOrderStatus(order.getId(), changeOrder);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getOrderStatus()).isEqualTo(changeOrder.getOrderStatus());
            softly.assertThat(result.getOrderLineItems()).containsAll(orderLineItems);
        });
    }

    @ParameterizedTest(name = "주문 상태를 {0}에서 {1}로 변경하면 예외를 던진다")
    @CsvSource(value = {"COMPLETION:MEAL", "COMPLETION:COOKING", "COMPLETION:COMPLETION"}, delimiter = ':')
    void 주문_상태를_변경하면_예외를_던진다(OrderStatus fromStatus, OrderStatus toStatus) {
        // given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(fromStatus.name());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        Order changeOrder = new Order();
        changeOrder.setOrderStatus(toStatus.name());

        given(orderDao.findById(order.getId()))
                .willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(order.getId()))
                .willReturn(orderLineItems);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
