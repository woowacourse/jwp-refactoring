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
import kitchenpos.dao.OrderRepositoryImpl;
import kitchenpos.dao.OrderTableRepositoryImpl;
import kitchenpos.dao.ProductRepositoryImpl;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
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

  private OrderTable orderTable;

  private Menu menu1, menu, menu3;
  private Product product;
  private MenuGroup menuGroup;
  private List<OrderLineItem> orderLineItems;

  @BeforeEach
  void setUp() {
    orderTable = orderTableRepository.save(OrderTableFixture.createNotEmptySingleOrderTable());

    menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
    product = productRepository.save(ProductFixture.createProduct());
    menu1 = menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menu = menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menu3 = menuRepository.save(MenuFixture.createMenu(menuGroup, product));

    orderLineItems = List.of(
        OrderLineItemFixture.createOrderLineItem(menu1),
        OrderLineItemFixture.createOrderLineItem(menu),
        OrderLineItemFixture.createOrderLineItem(menu3)
    );
  }

  @Test
  @DisplayName("create() : 주문을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Order order = OrderFixture.createMealOrderWithOrderLineItems(
        orderTable, orderLineItems
    );

    //when
    final Order savedOrder = orderService.create(order);

    //then
    final List<Long> orderLineItemIds = savedOrder.getOrderLineItems()
        .stream()
        .map(OrderLineItem::getSeq)
        .collect(Collectors.toList());

    assertAll(
        () -> assertNotNull(savedOrder.getId()),
        () -> assertThat(orderLineItemIds).doesNotContainNull()
    );
  }

  @Test
  @DisplayName("create() : 이미 비어있는 주문 테이블에서는 주문을 생성한다면 IllegalArgumentException가 발생할 수 있다..")
  void test_create_IllegalArgumentException() throws Exception {
    //given
    final OrderTable orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );

    final Order order = OrderFixture.createMealOrderWithOrderLineItems(
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
    final List<Order> savedOrders = orderService.list();

    //then
    assertEquals(4, savedOrders.size());
  }

  @Test
  @DisplayName("changeOrderStatus() : 주문 상태를 변경할 수 있다.")
  void test_changeOrderStatus() throws Exception {
    //given
    final Order order = orderRepository.save(
        OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));

    //when
    final Order updatedOrder = orderService.changeOrderStatus(order.getId(), order);

    //then
    assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
  }
}
