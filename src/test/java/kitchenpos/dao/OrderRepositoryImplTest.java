package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
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

  private TableGroup2 tableGroup;
  private OrderTable2 orderTable;

  @BeforeEach
  void setUp() {
    tableGroup = tableGroupRepository.save(TableGroupFixture.createTableGroup());
    orderTable = orderTableRepository.save(OrderTableFixture.createEmptyOrderTable(tableGroup));
  }

  @Test
  @DisplayName("save() : 주문을 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final Order2 order = OrderFixture.createMealOrder(orderTable);

    //when
    final Order2 savedOrder = orderRepository.save(order);

    //then
    assertAll(
        () -> assertNotNull(savedOrder.getId()),
        () -> assertThat(savedOrder)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(order)
    );
  }

  @Test
  @DisplayName("findById() : id를 통해 주문을 찾을 수 있다.")
  void test_findById() throws Exception {
    //given
    final Order2 order = orderRepository.save(OrderFixture.createMealOrder(orderTable));

    //when
    final Optional<Order2> savedOrder = orderRepository.findById(order.getId());

    //then
    assertAll(
        () -> assertTrue(savedOrder.isPresent()),
        () -> assertThat(savedOrder.get())
            .usingRecursiveComparison()
            .isEqualTo(order)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 주문들을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final Order2 order1 = orderRepository.save(OrderFixture.createMealOrder(orderTable));
    final Order2 order2 = orderRepository.save(OrderFixture.createMealOrder(orderTable));
    final Order2 order3 = orderRepository.save(OrderFixture.createMealOrder(orderTable));

    //when
    final List<Order2> orders = orderRepository.findAll();

    //then
    assertThat(orders)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(order1, order2, order3));
  }

  @Test
  @DisplayName("existsByOrderTableIdAndOrderStatusIn() : 주문 id와 주문 상태를 만족하는 주문이 존재하는지 확인할 수 있다.")
  void test_existsByOrderTableIdAndOrderStatusIn() throws Exception {
    //given
    final Order2 order = orderRepository.save(OrderFixture.createMealOrder(orderTable));

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
    final TableGroup2 tableGroup2 = tableGroupRepository.save(TableGroupFixture.createTableGroup());
    final OrderTable2 orderTable2 = orderTableRepository.save(
        OrderTableFixture.createEmptyOrderTable(tableGroup2)
    );

    orderRepository.save(OrderFixture.createMealOrder(orderTable));
    orderRepository.save(OrderFixture.createCompletionOrder(orderTable2));

    //when & then
    assertAll(
        () -> assertTrue(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            List.of(orderTable.getId(), orderTable2.getId()),
            List.of(OrderStatus.MEAL.name())
        )),
        () -> assertFalse(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            List.of(orderTable.getId(), orderTable2.getId()),
            List.of(OrderStatus.COOKING.name())
        ))
    );
  }
}
