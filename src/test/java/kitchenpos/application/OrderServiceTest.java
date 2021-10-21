package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("주문 서비스 테스트")
@MockitoSettings
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @DisplayName("주문을 생성한다. - 실패, 주문 항목이 비어있는 경우")
    @Test
    void createFailedWhenOrderLineItemsEmpty() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(emptyList());

        // when - then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(never())
                .countByIdIn(anyList());
        then(orderTableDao).should(never())
                .findById(order.getOrderTableId());
        then(orderDao).should(never())
                .findById(any());
        then(orderDao).should(never())
                .save(any(Order.class));
    }

    @DisplayName("주문을 생성한다. - 실패, 주문 항목이 null인 경우")
    @Test
    void createFailedWhenOrderLineItemsNull() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(null);

        // when - then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(never())
                .countByIdIn(anyList());
        then(orderTableDao).should(never())
                .findById(order.getOrderTableId());
        then(orderDao).should(never())
                .findById(any());
        then(orderDao).should(never())
                .save(any(Order.class));
    }

    @DisplayName("주문을 생성한다. - 실패, 주문 항목의 개수와 주문 항목들의 메뉴 아이디로 조회한 개수가 다른 경우")
    @Test
    void createFailedWhenSizeNotEqual() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setQuantity(1L);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setQuantity(1L);
        orderLineItem2.setMenuId(1L); // menuId는 1L만 등록되어있음.
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        final List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn(1L);

        // when - then
        // menuIds는 1이지만, orderLineItems의 사이즈는 2인 경우
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(times(1))
                .countByIdIn(menuIds);
        then(orderTableDao).should(never())
                .findById(order.getOrderTableId());
        then(orderDao).should(never())
                .findById(any());
        then(orderDao).should(never())
                .save(any(Order.class));
    }

    @DisplayName("주문을 생성한다. - 실패, 주문에 등록된 TableId가 존재하지 않는 경우")
    @Test
    void createFailedWhenTableIdNotFound() {
        // given
        Order order = new Order();
        order.setOrderTableId(-1L);

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setQuantity(1L);
        orderLineItem1.setMenuId(1L);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setQuantity(1L);
        orderLineItem2.setMenuId(2L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        final List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(times(1))
                .countByIdIn(menuIds);
        then(orderTableDao).should(times(1))
                .findById(order.getOrderTableId());
        then(orderDao).should(never())
                .findById(any());
        then(orderDao).should(never())
                .save(any(Order.class));
    }

    @DisplayName("주문을 생성한다. - 실패, 주문에 등록된 테이블이 비어있는 경우")
    @Test
    void createFailedWhenTableIsEmpty() {
        // given
        Order order = new Order();
        order.setOrderTableId(-1L);

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setQuantity(1L);
        orderLineItem1.setMenuId(1L);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setQuantity(1L);
        orderLineItem2.setMenuId(2L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        final List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn(2L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when - then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(times(1))
                .countByIdIn(menuIds);
        then(orderTableDao).should(times(1))
                .findById(order.getOrderTableId());
        then(orderDao).should(never())
                .findById(any());
        then(orderDao).should(never())
                .save(any(Order.class));
    }

    @DisplayName("주문 상태를 변경한다. - 실패, orderId에 해당하는 주문이 존재하지 않는 경우")
    @Test
    void changeOrderStatusFailedWhenOrderIdNotFound() {
        // given
        Long orderId = -1L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        given(orderDao.findById(orderId)).willThrow(IllegalArgumentException.class);

        // when -  then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderDao).should(times(1))
                .findById(orderId);
        then(orderDao).should(never())
                .save(any());
    }

    @DisplayName("주문 상태를 변경한다. - 실패, orderId에 해당하는 주문이 이미 COMPLETION 상태인 경우")
    @Test
    void changeOrderStatusFailedWhenStatusIsCompletion() {
        // given
        Long orderId = 1L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());

        Order savedOrder = new Order();
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));

        // when -  then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderDao).should(times(1))
                .findById(orderId);
        then(orderDao).should(never())
                .save(savedOrder);
    }
}
