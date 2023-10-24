package kitchenpos.table_group.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.infrastructure.persistence.MenuRepositoryImpl;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.infrastructure.persistence.MenuGroupRepositoryImpl;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.infrastructure.persistence.OrderRepositoryImpl;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.infrastructure.persistence.OrderTableRepositoryImpl;
import kitchenpos.product.domain.Product;
import kitchenpos.product.infrastructure.persistence.ProductRepositoryImpl;
import kitchenpos.support.ServiceIntegrationTest;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.infrastructure.persistence.TableGroupRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceIntegrationTest {

  @Autowired
  private TableGroupService tableGroupService;

  @Autowired
  private OrderTableRepositoryImpl orderTableRepository;

  @Autowired
  private TableGroupRepositoryImpl tableGroupRepository;

  @Autowired
  private OrderRepositoryImpl orderRepository;

  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepository;

  @Autowired
  private ProductRepositoryImpl productRepository;

  @Autowired
  private MenuRepositoryImpl menuRepository;

  private OrderTable orderTable1, orderTable, orderTable3;
  private List<OrderLineItem> orderLineItems;
  private Menu menu1, menu, menu3;
  private Product product;
  private MenuGroup menuGroup;

  @BeforeEach
  void setUp() {
    orderTable1 = orderTableRepository.save(OrderTableFixture.createEmptySingleOrderTable());
    orderTable = orderTableRepository.save(OrderTableFixture.createEmptySingleOrderTable());
    orderTable3 = orderTableRepository.save(OrderTableFixture.createEmptySingleOrderTable());

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
  @DisplayName("create() : 테이블 그룹을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final List<OrderTable> orderTables = List.of(orderTable1, orderTable, orderTable3);
    final TableGroup tableGroup = TableGroupFixture.createTableGroup(orderTables);

    final List<Long> tableGroupIds = orderTables
        .stream()
        .map(OrderTable::getTableGroupId)
        .collect(Collectors.toList());

    assertThat(tableGroupIds).containsNull();

    //when
    final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

    //then
    final List<Long> afterSavedTableGroupIds = savedTableGroup.getOrderTables()
        .stream()
        .map(OrderTable::getTableGroupId)
        .collect(Collectors.toList());

    assertAll(
        () -> assertNotNull(savedTableGroup.getId()),
        () -> assertThat(afterSavedTableGroupIds).doesNotContainNull()
    );
  }

  @Test
  @DisplayName("create() : 주문 테이블이 2개 미만일 때 테이블 그룹을 생성할 시 IllegalArgumentException가 발생할 수 있다.")
  void test_create_IllegalArgumentException() throws Exception {
    //given
    final List<OrderTable> orderTables = List.of(orderTable1);
    final TableGroup tableGroup = TableGroupFixture.createTableGroup(orderTables);

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroup))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("create() : 해당 주문 테이블이 비어있지 않거나 이미 다른 그룹에 속해있다면 IllegalArgumentException가 발생할 수 있다.")
  void test_create_IllegalArgumentException2() throws Exception {
    //given
    final OrderTable emptyNotBelongOrderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable());

    final OrderTable notEmptyNotBelongOrderTable = orderTableRepository.save(
        OrderTableFixture.createNotEmptySingleOrderTable()
    );

    final TableGroup tableGroup = TableGroupFixture.createTableGroup(
        List.of(emptyNotBelongOrderTable, notEmptyNotBelongOrderTable)
    );

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroup))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("ungroup() : 주문 테이블을 주문 그룹으로부터 분리할 수 있다.")
  void test_ungroup() throws Exception {
    //given
    final List<OrderTable> orderTables = List.of(orderTable1, orderTable, orderTable3);
    final TableGroup savedTableGroup = tableGroupRepository.save(
        TableGroupFixture.createTableGroup(orderTables)
    );

    final List<Long> afterSavedTableGroupIds = savedTableGroup.getOrderTables()
        .stream()
        .map(OrderTable::getTableGroupId)
        .collect(Collectors.toList());

    assertThat(afterSavedTableGroupIds).doesNotContainNull();

    //when
    tableGroupService.ungroup(savedTableGroup.getId());

    //then
    final TableGroup unGroupTableGroup =
        tableGroupRepository.findById(savedTableGroup.getId()).get();

    final List<Long> afterUngroupTableGroupIds = unGroupTableGroup.getOrderTables()
        .stream()
        .map(OrderTable::getTableGroupId)
        .collect(Collectors.toList());

    final List<Boolean> afterUngroupOrderTableEmpty = unGroupTableGroup.getOrderTables()
        .stream()
        .map(OrderTable::isEmpty)
        .collect(Collectors.toList());

    assertAll(
        () -> assertThat(afterUngroupTableGroupIds)
            .allMatch(Objects::isNull),
        () -> assertThat(afterUngroupOrderTableEmpty)
            .allMatch(it -> !it)
    );
  }

  @Test
  @DisplayName("ungroup() : 주문 그룹으로부터 주문 테이블들을 분리할 때 상태가 COOKING이거나 MEAL일 경우에는 주문 테이블을 분리할 수 없다.")
  void test_ungroup_IllegalArgumentException() throws Exception {
    //given
    orderRepository.save(
        OrderFixture.createMealOrderWithOrderLineItems(orderTable1, orderLineItems));
    final List<OrderTable> orderTables = List.of(orderTable1, orderTable);
    final TableGroup tableGroup = tableGroupRepository.save(
        TableGroupFixture.createTableGroup(orderTables));

    //when & then
    assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
        .isInstanceOf(IllegalArgumentException.class);
  }
}

