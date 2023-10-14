package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceIntegrationTest {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderLineItemDao orderLineItemDao;

  @Autowired
  private OrderDao orderDao;

  @Test
  @DisplayName("create() : 주문을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Order order = createSuccessfulOrder();

    //when
    final Order savedOrder = orderService.create(order);

    //then
    assertAll(
        () -> assertNotNull(savedOrder.getId()),
        () -> assertEquals(savedOrder.getOrderStatus(), OrderStatus.COOKING.name()),
        () -> assertNotNull(savedOrder.getOrderedTime())
    );
  }

  private Order createSuccessfulOrder() {
    final OrderLineItem orderLineItem1 = new OrderLineItem();
    orderLineItem1.setQuantity(13);
    orderLineItem1.setMenuId(1L);
    final OrderLineItem orderLineItem2 = new OrderLineItem();
    orderLineItem2.setQuantity(3);
    orderLineItem2.setMenuId(2L);

    final Order order = new Order();
    final long orderTableId = 335L;
    order.setOrderTableId(orderTableId);
    order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));
    return order;
  }

  @Test
  @DisplayName("create() : 이미 비어있는 주문 테이블에서는 주문을 생성한다면 IllegalArgumentException가 발생할 수 있다..")
  void test_create_IllegalArgumentException() throws Exception {
    //given
    final OrderLineItem orderLineItem1 = new OrderLineItem();
    orderLineItem1.setQuantity(13);
    orderLineItem1.setMenuId(1L);
    final OrderLineItem orderLineItem2 = new OrderLineItem();
    orderLineItem2.setQuantity(3);
    orderLineItem2.setMenuId(2L);

    final Order order = new Order();
    final long orderTableId = 337L;
    order.setOrderTableId(orderTableId);
    order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

    //when & then
    assertThatThrownBy(() -> orderService.create(order))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("list() : 모든 주문들을 조회할 수 있다.")
  void test_list() throws Exception {
    //given
    final int beforeSize = orderService.list().size();

    orderService.create(createSuccessfulOrder());

    //when
    final List<Order> savedOrders = orderService.list();

    //then
    assertEquals(savedOrders.size(), beforeSize + 1);
  }

  @Test
  @DisplayName("changeOrderStatus() : 주문 상태를 변경할 수 있다.")
  void test_changeOrderStatus() throws Exception {
    //given
    final long savedOrderId = 3333L;

    final Order order = new Order();
    order.setOrderStatus(OrderStatus.COMPLETION.name());

    //when
    final Order updatedOrder = orderService.changeOrderStatus(savedOrderId, order);

    //then
    assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
  }
}
