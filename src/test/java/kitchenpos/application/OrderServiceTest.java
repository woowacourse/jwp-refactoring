package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepositoryImpl;
import kitchenpos.dao.MenuRepositoryImpl;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderRepositoryImpl;
import kitchenpos.dao.OrderTableRepositoryImpl;
import kitchenpos.dao.ProductRepositoryImpl;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.Product2;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceIntegrationTest {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderTableRepositoryImpl orderTableRepository;

  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepository;

  @Autowired
  private ProductRepositoryImpl productRepository;

  @Autowired
  private MenuRepositoryImpl menuRepository;

  @Autowired
  private OrderRepositoryImpl orderRepository;

  private OrderTable2 orderTable;

  private Menu2 menu1, menu2, menu3;
  private Product2 product;
  private MenuGroup menuGroup;
  private List<OrderLineItem2> orderLineItems;

  @BeforeEach
  void setUp() {
    orderTable = orderTableRepository.save(OrderTableFixture.createNotEmptySingleOrderTable());

    menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
    product = productRepository.save(ProductFixture.createProduct());
    menu1 = menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menu2 = menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menu3 = menuRepository.save(MenuFixture.createMenu(menuGroup, product));

    orderLineItems = List.of(
        OrderLineItemFixture.createOrderLineItem(menu1),
        OrderLineItemFixture.createOrderLineItem(menu2),
        OrderLineItemFixture.createOrderLineItem(menu3)
    );
  }

  @Test
  @DisplayName("create() : 주문을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Order2 order = OrderFixture.createMealOrderWithOrderLineItems(
        orderTable, orderLineItems
    );

    //when
    final Order2 savedOrder = orderService.create(order);

    //then
    final List<Long> orderLineItemIds = savedOrder.getOrderLineItems()
        .stream()
        .map(OrderLineItem2::getSeq)
        .collect(Collectors.toList());

    assertAll(
        () -> assertNotNull(savedOrder.getId()),
        () -> assertThat(orderLineItemIds).doesNotContainNull()
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
    final OrderTable2 orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );

    final Order2 order = OrderFixture.createMealOrderWithOrderLineItems(
        orderTable, orderLineItems
    );

    //when & then
    assertThatThrownBy(() -> orderService.create(order))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("list() : 모든 주문들을 조회할 수 있다.")
  void test_list() throws Exception {
    //given
    orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));
    orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));
    orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));
    orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));

    //when
    final List<Order2> savedOrders = orderService.list();

    //then
    assertEquals(4, savedOrders.size());
  }

  @Test
  @DisplayName("changeOrderStatus() : 주문 상태를 변경할 수 있다.")
  void test_changeOrderStatus() throws Exception {
    //given
    final Order2 order = orderRepository.save(
        OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));

    //when
    final Order2 updatedOrder = orderService.changeOrderStatus(order.getId(), order);

    //then
    assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
  }
}
