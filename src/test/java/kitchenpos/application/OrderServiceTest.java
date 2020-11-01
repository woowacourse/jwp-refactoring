package kitchenpos.application;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.factory.OrderFactory;
import kitchenpos.factory.OrderLineItemFactory;
import kitchenpos.factory.OrderTableFactory;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    private OrderFactory orderFactory = new OrderFactory();
    private OrderLineItemFactory orderLineItemFactory = new OrderLineItemFactory();
    private OrderTableFactory orderTableFactory = new OrderTableFactory();

    @DisplayName("주문 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("주문을 생성한다.", this::createSuccess),
                dynamicTest("주문 항목이 존재해야 한다.", this::noOrderLineItems),
                dynamicTest("주문 항목은 하나의 메뉴를 가진다.", this::orderLineItemsAndMenuMismatch),
                dynamicTest("테이블이 존재해야 한다.", this::noOrderTable),
                dynamicTest("테이블은 빈 테이블 일 수 없다.", this::emptyOrderTable)
        );
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        Order order1 = orderFactory.create(1L);
        Order order2 = orderFactory.create(2L);
        OrderLineItem orderLineItem1 = orderLineItemFactory.create(1L, 1L, 1L, 1);
        OrderLineItem orderLineItem2 = orderLineItemFactory.create(2L, 1L, 2L, 1);
        OrderLineItem orderLineItem3 = orderLineItemFactory.create(3L, 2L, 3L, 2);

        given(orderDao.findAll()).willReturn(asList(order1, order2));
        given(orderLineItemDao.findAllByOrderId(order1.getId()))
                .willReturn(asList(orderLineItem1, orderLineItem2));
        given(orderLineItemDao.findAllByOrderId(order2.getId()))
                .willReturn(singletonList(orderLineItem3));

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.get(0).getId()).isEqualTo(1L),
                () -> assertThat(orders.get(0).getOrderLineItems().size()).isEqualTo(2),
                () -> assertThat(orders.get(1).getId()).isEqualTo(2L),
                () -> assertThat(orders.get(1).getOrderLineItems().size()).isEqualTo(1)
        );
    }

    @DisplayName("주문 상태 변경")
    @TestFactory
    Stream<DynamicTest> changeOrderStatus() {
        return Stream.of(
                dynamicTest("주문 상태를 변경한다.", this::changeOrderStatusSuccess),
                dynamicTest("주문이 존재해야 한다.", this::noOrder),
                dynamicTest("주문의 상태가 완료일 수 없다.", this::invalidOrder)
        );
    }

    private void createSuccess() {
        OrderLineItem orderLineItem = orderLineItemFactory.create(1L, 1);
        Order order = orderFactory.create(1L, singletonList(orderLineItem));
        OrderTable orderTable = orderTableFactory.create(1L, false);

        given(menuRepository.countByIdIn(singletonList(orderLineItem.getMenuId()))).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order))
                .willReturn(
                        orderFactory.create(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                                singletonList(orderLineItem)));
        given(orderLineItemDao.save(orderLineItem)).willReturn(
                orderLineItemFactory.create(1L, 1L, 1L, 1));

        Order saved = orderService.create(order);

        assertAll(
                () -> assertThat(saved.getId()).isEqualTo(1L),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(saved.getOrderedTime()).isNotNull(),
                () -> assertThat(saved.getOrderLineItems().get(0).getOrderId()).isEqualTo(
                        orderLineItem.getOrderId()),
                () -> assertThat(saved.getOrderTableId()).isEqualTo(orderTable.getId())
        );
    }

    private void noOrderLineItems() {
        Order order = orderFactory.create(1L, emptyList());

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    private void orderLineItemsAndMenuMismatch() {
        OrderLineItem orderLineItem = orderLineItemFactory.create(1L, 1);
        Order order = orderFactory.create(1L, singletonList(orderLineItem));

        given(menuRepository.countByIdIn(singletonList(orderLineItem.getMenuId()))).willReturn(2L);

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    private void noOrderTable() {
        OrderLineItem orderLineItem = orderLineItemFactory.create(1L, 1);
        Order order = orderFactory.create(1L, singletonList(orderLineItem));

        given(menuRepository.countByIdIn(singletonList(orderLineItem.getMenuId()))).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    private void emptyOrderTable() {
        OrderLineItem orderLineItem = orderLineItemFactory.create(1L, 1);
        Order order = orderFactory.create(1L, singletonList(orderLineItem));
        OrderTable orderTable = orderTableFactory.create(1L, true);

        given(menuRepository.countByIdIn(singletonList(orderLineItem.getMenuId()))).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    private void changeOrderStatusSuccess() {
        Long orderId = 1L;
        Order order = orderFactory.create(OrderStatus.COMPLETION.name());
        Order saved = orderFactory.create(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(),
                emptyList());
        Order changed = orderFactory.create(1L, 1L, order.getOrderStatus(), LocalDateTime.now(),
                emptyList());
        OrderLineItem orderLineItem = orderLineItemFactory.create(1L, 1L, 1L, 1);

        given(orderDao.findById(orderId)).willReturn(Optional.of(saved));
        given(orderDao.save(saved)).willReturn(changed);
        given(orderLineItemDao.findAllByOrderId(saved.getId()))
                .willReturn(singletonList(orderLineItem));

        Order response = orderService.changeOrderStatus(orderId, order);
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(changed.getId()),
                () -> assertThat(response.getOrderStatus()).isEqualTo(changed.getOrderStatus()),
                () -> assertThat(response.getOrderLineItems().get(0)).isEqualTo(orderLineItem)
        );
    }

    private void noOrder() {
        Long orderId = 1L;
        Order order = orderFactory.create(OrderStatus.COMPLETION.name());

        given(orderDao.findById(orderId)).willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderId, order));
    }

    private void invalidOrder() {
        Long orderId = 1L;
        Order order = orderFactory.create(OrderStatus.COMPLETION.name());
        Order saved = orderFactory.create(1L, 1L, OrderStatus.COMPLETION.name(),
                LocalDateTime.now(), emptyList());

        given(orderDao.findById(orderId)).willReturn(Optional.of(saved));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderId, order));
    }
}