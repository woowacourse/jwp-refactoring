package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.support.JdbcTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderLineItemRepositoryImplTest extends JdbcTestHelper {

  @Autowired
  private OrderLineItemRepositoryImpl orderLineItemRepository;

  @Autowired
  private MenuRepositoryImpl menuRepository;

  @Autowired
  private OrderRepositoryImpl orderRepository;

  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepository;

  @Autowired
  private TableGroupRepositoryImpl tableGroupRepository;

  @Autowired
  private OrderTableRepositoryImpl orderTableRepository;

  private MenuGroup menuGroup;
  private Menu2 menu;
  private TableGroup2 tableGroup;
  private OrderTable2 orderTable;
  private Order2 order;

  @BeforeEach
  void setUp() {
    menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
    menu = menuRepository.save(MenuFixture.createMenu(menuGroup));

    tableGroup = tableGroupRepository.save(TableGroupFixture.createTableGroup());
    orderTable = orderTableRepository.save(OrderTableFixture.createEmptyOrderTable(tableGroup));
    order = orderRepository.save(OrderFixture.createMealOrder(orderTable));

  }

  @Test
  @DisplayName("save() : 주문에 속한 아이템들을 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final OrderLineItem2 orderLineItem = OrderLineItemFixture.createOrderLineItem(menu);

    //when
    final OrderLineItem2 savedOrderLineItem = orderLineItemRepository.save(orderLineItem, order);

    //then
    assertAll(
        () -> assertNotNull(savedOrderLineItem.getSeq()),
        () -> assertThat(savedOrderLineItem)
            .usingRecursiveComparison()
            .ignoringFields("seq")
            .isEqualTo(orderLineItem)
    );
  }

  @Test
  @DisplayName("findById() : id를 통해 주문에 속한 아이템을 조회할 수 있다.")
  void test_findById() throws Exception {
    //given
    final OrderLineItem2 orderLineItem = orderLineItemRepository.save(
        OrderLineItemFixture.createOrderLineItem(menu), order);

    //when
    final Optional<OrderLineItem2> savedOrderLineItem = orderLineItemRepository.findById(
        orderLineItem.getSeq()
    );

    //then
    assertAll(
        () -> assertTrue(savedOrderLineItem.isPresent()),
        () -> assertThat(savedOrderLineItem.get())
            .usingRecursiveComparison()
            .isEqualTo(orderLineItem)
    );
  }

  @Test
  @DisplayName("findAll() : 저장된 모든 주문에 속한 아이템을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final OrderLineItem2 orderLineItem1 = orderLineItemRepository.save(
        OrderLineItemFixture.createOrderLineItem(menu), order);
    final OrderLineItem2 orderLineItem2 = orderLineItemRepository.save(
        OrderLineItemFixture.createOrderLineItem(menu), order);

    //when
    final List<OrderLineItem2> orderLineItems = orderLineItemRepository.findAll();

    //then
    assertThat(orderLineItems)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(orderLineItem1, orderLineItem2));
  }

  @Test
  @DisplayName("findAllByOrderId() : 주문에 속한 주문 아이템들을 모두 조회할 수 있다.")
  void test_findAllByOrderId() throws Exception {
    //given
    final OrderLineItem2 orderLineItem1 = orderLineItemRepository.save(
        OrderLineItemFixture.createOrderLineItem(menu), order);
    final OrderLineItem2 orderLineItem2 = orderLineItemRepository.save(
        OrderLineItemFixture.createOrderLineItem(menu), order);

    final TableGroup2 tableGroup2 = tableGroupRepository.save(TableGroupFixture.createTableGroup());
    final OrderTable2 orderTable2 = orderTableRepository.save(
        OrderTableFixture.createEmptyOrderTable(tableGroup2)
    );
    final Order2 order2 = orderRepository.save(OrderFixture.createMealOrder(orderTable2));

    final OrderLineItem2 orderLineItem3 = orderLineItemRepository.save(
        OrderLineItemFixture.createOrderLineItem(menu), order2);

    //when
    final List<OrderLineItem2> orderLineItems = orderLineItemRepository.findAllByOrderId(
        order.getId()
    );

    //then
    assertThat(orderLineItems)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(orderLineItem1, orderLineItem2));
  }
}
