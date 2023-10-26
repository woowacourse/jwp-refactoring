package kitchenpos.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
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
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        OrderTable orderTable = new OrderTable(1L, null, 0, false);

        Order order = new Order(1L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.save(any()))
                .willReturn(order);
        given(orderLineItemDao.save(any()))
                .willReturn(orderLineItem);

        // when
        OrderDto result = orderService.create(OrderDto.from(order));

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
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        OrderTable orderTable = new OrderTable(1L, null, 0, false);

        Order order1 = new Order(1L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        Order order2 = new Order(2L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        given(orderDao.findAll())
                .willReturn(List.of(order1, order2));

        // when
        List<OrderDto> result = orderService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @ParameterizedTest(name = "주문 상태를 {0}에서 {1}로 변경한다")
    @CsvSource(value = {"COOKING:MEAL", "COOKING:COMPLETION", "MEAL:COMPLETION", "COOKING:COOKING", "MEAL:MEAL"}, delimiter = ':')
    void 주문_상태를_변경한다(OrderStatus fromStatus, OrderStatus toStatus) {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        Order order = new Order(1L, fromStatus.name(), LocalDateTime.now(), orderLineItems);
        Order changeOrder = new Order(1L, toStatus.name(), LocalDateTime.now(), orderLineItems);

        given(orderDao.findById(any()))
                .willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(any()))
                .willReturn(orderLineItems);

        // when
        OrderDto result = orderService.changeOrderStatus(order.getId(), OrderDto.from(changeOrder));

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
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        Order order = new Order(1L, fromStatus.name(), LocalDateTime.now(), orderLineItems);
        Order changeOrder = new Order(1L, toStatus.name(), LocalDateTime.now(), orderLineItems);

        given(orderDao.findById(any()))
                .willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(any()))
                .willReturn(orderLineItems);


        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderDto.from(changeOrder)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
