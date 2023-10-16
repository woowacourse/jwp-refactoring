package kitchenpos.order_table.application;

import static kitchenpos.fixture.OrderFixture.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.order.application.OrderService;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import kitchenpos.table_group.application.TableGroupService;
import kitchenpos.table_group.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrateTest {

  @Autowired
  private OrderService orderService;
  @Autowired
  private OrderTableDao orderTableDao;
  @Autowired
  private TableGroupService tableGroupService;
  @Autowired
  private TableService tableService;

  private OrderTable table1;
  private OrderTable table2;

  @BeforeEach
  void init() {
    table1 = orderTableDao.findById(1L).get();
    table2 = orderTableDao.findById(2L).get();
  }

  @Test
  @DisplayName("테이블을 등록할 수 있다.")
  void create_success() {
    //given
    final OrderTable table = new OrderTable(null, null, 0, true);

    //when
    final OrderTable actual = tableService.create(table);

    //then
    assertThat(actual).isNotNull();

  }

  @Test
  @DisplayName("테이블 목록을 조회할 수 있다.")
  void list_success() {
    //given, when
    final List<OrderTable> actual = tableService.list();

    //then
    assertThat(actual).hasSize(8);

  }


  @Test
  @DisplayName("테이블이 비었는지 여부를 변경할 수 있다.")
  void changeEmpty_success() {
    //given
    final boolean expected = false;
    final OrderTable changedTable = new OrderTable(null, null, 0, expected);

    //when
    final boolean actual = tableService.changeEmpty(table1.getId(), changedTable).isEmpty();

    //then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DisplayName("테이블이 비었는지 여부를 변경할 때 대상 테이블이 존재하지 않으면 예외를 반환한다.")
  void changeEmpty_fail_not_exist_table() {
    //given
    final OrderTable changedTable = new OrderTable(null, null, 0, false);

    //when
    final ThrowingCallable actual = () -> tableService.changeEmpty(999L, changedTable);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("테이블이 비었는지 여부를 변경할 때 대상 테이블이 속한 단체 테이블이 있으면 예외를 반환한다.")
  void changeEmpty_fail_in_tableGroup() {
    //given
    final TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(),
        List.of(table1, table2));
    tableGroupService.create(tableGroup);

    final OrderTable changedTable = new OrderTable(null, null, 0, false);

    //when
    final ThrowingCallable actual = () -> tableService.changeEmpty(table1.getId(), changedTable);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("테이블이 비었는지 여부를 변경할 때 대상 테이블의 주문 중 계산이 완료되지 않은 주문이 있으면 예외를 반환한다.")
  void changeEmpty_fail_not_COMPLETION_order() {
    //given
    orderTableDao.save(new OrderTable(1L, null, 4, false));

    orderService.create(주문(table1.getId()));
    final OrderTable changedTable = new OrderTable(null, null, 0, true);

    //when
    final ThrowingCallable actual = () -> tableService.changeEmpty(table1.getId(), changedTable);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("테이블의 손님 수를 변경할 수 있다.")
  void changeNumberOfGuests_success() {
    //given
    final int expected = 4;
    final OrderTable changedTable = orderTableDao.save(
        new OrderTable(table1.getId(), null, expected, false));

    //when
    final int actual = tableService.changeNumberOfGuests(table1.getId(), changedTable)
        .getNumberOfGuests();

    //then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DisplayName("테이블의 손님 수를 변경할 때 변경하려는 손님 수가 0 이하면 예외를 반환한다.")
  void changeNumberOfGuests_fail_not_multiple() {
    //given
    final OrderTable changedTable = new OrderTable(null, null, -1, false);

    //when
    final ThrowingCallable actual = () -> tableService.changeNumberOfGuests(table1.getId(),
        changedTable);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("테이블의 손님 수를 변경할 때 대상 테이블이 존재하지 않으면 예외를 반환한다.")
  void changeNumberOfGuests_fail_not_exist_table() {
    //given
    final OrderTable changedTable = new OrderTable(null, null, 4, false);

    //when
    final ThrowingCallable actual = () -> tableService.changeNumberOfGuests(999L, changedTable);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("테이블의 손님 수를 변경할 때 대상 테이블이 비어있으면 예외를 반환한다.")
  void changeNumberOfGuests_fail_empty_table() {
    //given
    final OrderTable changedTable = new OrderTable(null, null, 4, false);

    //when
    final ThrowingCallable actual = () -> tableService.changeNumberOfGuests(table1.getId(),
        changedTable);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

}
