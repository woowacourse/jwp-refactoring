package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.support.JdbcTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderRepositoryImplTest extends JdbcTestHelper {

  @Autowired
  private OrderRepositoryImpl orderRepository;

  @Autowired
  private OrderTableRepositoryImpl orderTableRepository;

  @Autowired
  private TableGroupRepositoryImpl tableGroupRepository;

  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepository;

  @Autowired
  private ProductRepositoryImpl productRepository;

  @Autowired
  private MenuRepositoryImpl menuRepository;

  private TableGroup tableGroup;
  private OrderTable orderTable;
  private Menu menu1, menu, menu3;
  private Product product;
  private MenuGroup menuGroup;
  private List<OrderLineItem> orderLineItems;

  @BeforeEach
  void setUp() {
    orderTable = orderTableRepository.save(OrderTableFixture.createEmptySingleOrderTable());
    tableGroup = tableGroupRepository.save(TableGroupFixture.createTableGroup(List.of(orderTable)));

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
  @DisplayName("save() : 주문을 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final List<OrderLineItem> orderLineItems = List.of(
        OrderLineItemFixture.createOrderLineItem(menu1),
        OrderLineItemFixture.createOrderLineItem(menu),
        OrderLineItemFixture.createOrderLineItem(menu3)
    );
    final Order order = OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems);

    //when
    final Order savedOrder = orderRepository.save(order);

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
  @DisplayName("findById() : id를 통해 주문을 찾을 수 있다.")
  void test_findById() throws Exception {
    //given
    final OrderTable orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );
    final Order order = orderRepository.save(
        OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems)
    );

    //when
    final Optional<Order> savedOrder = orderRepository.findById(order.getId());

    System.out.println(savedOrder.get().getOrderTable().getTableGroupId());
    System.out.println(
        "order.getOrderTable().getTableGroupId() = " + order.getOrderTable().getTableGroupId()
    );

    //then
    assertAll(
        () -> assertTrue(savedOrder.isPresent()),
        () -> assertThat(savedOrder.get())
            .usingRecursiveComparison()
            .ignoringFields("orderLineItems")
            .isEqualTo(order)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 주문들을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final Order order1 = orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));
    final Order order = orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));
    final Order order3 = orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));

    //when
    final List<Order> orders = orderRepository.findAll();

    //then
    assertThat(orders)
        .extracting("id")
        .containsExactlyInAnyOrderElementsOf(
            List.of(order1.getId(), order.getId(), order3.getId())
        );
  }

  @Test
  @DisplayName("existsByOrderTableIdAndOrderStatusIn() : 주문 id와 주문 상태를 만족하는 주문이 존재하는지 확인할 수 있다.")
  void test_existsByOrderTableIdAndOrderStatusIn() throws Exception {
    //given
    final Order order = orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));

    //when & then
    assertAll(
        () -> assertTrue(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(),
            List.of(OrderStatus.MEAL.name())
        )),
        () -> assertFalse(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(),
            List.of(OrderStatus.COMPLETION.name(), OrderStatus.COOKING.name())
        ))
    );
  }

  @Test
  @DisplayName("existsByOrderTableIdInAndOrderStatusIn() : 주문 id들과 주문 상태를 만족하는 주문들이 존재하는지 확인할 수 있다.")
  void test_existsByOrderTableIdInAndOrderStatusIn() throws Exception {
    //given
    final OrderTable orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable()
    );

    orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(this.orderTable, orderLineItems));
    orderRepository.save(OrderFixture.createCompletionOrderWithOrderLineItems(orderTable, orderLineItems));

    //when & then
    assertAll(
        () -> assertTrue(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            List.of(this.orderTable.getId(), orderTable.getId()),
            List.of(OrderStatus.MEAL.name())
        )),
        () -> assertFalse(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            List.of(this.orderTable.getId(), orderTable.getId()),
            List.of(OrderStatus.COOKING.name())
        ))
    );
  }
}
