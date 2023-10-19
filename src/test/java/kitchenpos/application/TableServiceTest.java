package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.dao.MenuGroupRepositoryImpl;
import kitchenpos.dao.MenuRepositoryImpl;
import kitchenpos.dao.OrderRepositoryImpl;
import kitchenpos.dao.OrderTableRepositoryImpl;
import kitchenpos.dao.ProductRepositoryImpl;
import kitchenpos.dao.TableGroupRepositoryImpl;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.Product2;
import kitchenpos.domain.TableGroup2;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrationTest {

  @Autowired
  private TableService tableService;

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

  private TableGroup2 tableGroup;
  private OrderTable2 orderTable;

  private Menu2 menu1, menu2, menu3;
  private Product2 product;
  private MenuGroup menuGroup;
  private List<OrderLineItem2> orderLineItems;


  @BeforeEach
  void setUp() {
    orderTable = orderTableRepository.save(OrderTableFixture.createNotEmptySingleOrderTable());
    tableGroup = tableGroupRepository.save(TableGroupFixture.createTableGroup(List.of(orderTable)));

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
  @DisplayName("create() : 주문 테이블을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final OrderTable2 orderTable = OrderTableFixture.createNotEmptySingleOrderTable();

    //when
    final OrderTable2 savedOrderTable = tableService.create(orderTable);

    //then
    assertNotNull(savedOrderTable.getId());
  }

  @Test
  @DisplayName("list() : 모든 주문 테이블을 조회할 수 있다.")
  void test_list() throws Exception {
    //given
    orderTableRepository.save(OrderTableFixture.createEmptySingleOrderTable());
    orderTableRepository.save(OrderTableFixture.createEmptySingleOrderTable());
    orderTableRepository.save(OrderTableFixture.createNotEmptySingleOrderTable());
    orderTableRepository.save(OrderTableFixture.createNotEmptySingleOrderTable());

    //when
    final List<OrderTable2> orderTables = tableService.list();

    //then
    assertEquals(5, orderTables.size());
  }

  @Test
  @DisplayName("changeEmpty() : 주문 테이블의 empty 상태를 변경할 수 있다.")
  void test_changeEmpty() throws Exception {
    //given
    final OrderTable2 orderTable = orderTableRepository.save(
        OrderTableFixture.createNotEmptySingleOrderTable()
    );

    //when
    final OrderTable2 updatedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

    //then
    assertEquals(orderTable.isEmpty(), updatedOrderTable.isEmpty());
  }

  @Test
  @DisplayName("changeEmpty() : 주문 테이블에 주문이 존재하고 그 상태가 COOKING이나 MEAL이면 empty를 변경할 수 없다.")
  void test_changeEmpty_IllegalArgumentException() throws Exception {
    //given
    orderRepository.save(OrderFixture.createMealOrderWithOrderLineItems(orderTable, orderLineItems));

    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("changeNumberOfGuests() : 주문 테이블의 인원 수를 수정할 수 있다.")
  void test_changeNumberOfGuests() throws Exception {
    //when
    final OrderTable2 updatedOrderTable = tableService.changeNumberOfGuests(
        orderTable.getId(), orderTable
    );

    //then
    assertEquals(updatedOrderTable.getNumberOfGuests(), orderTable.getNumberOfGuests());
  }

  @Test
  @DisplayName("changeNumberOfGuests() : 특정 주문 테이블이 비어있으면 인원 수를 수정할 수 없다.")
  void test_changeNumberOfGuests_IllegalArgumentException() throws Exception {
    //given
    final OrderTable2 orderTable = orderTableRepository.save(
        OrderTableFixture.createEmptySingleOrderTable());

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
