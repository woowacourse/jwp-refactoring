package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

class TableServiceTest extends ServiceIntegrationTest {

  @Autowired
  private TableService tableService;

  @Autowired
  private OrderTableDao orderTableDao;

  @Test
  @DisplayName("create() : 주문 테이블을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final OrderTable orderTable = new OrderTable();

    //when
    final OrderTable savedOrderTable = tableService.create(orderTable);

    //then
    assertNotNull(savedOrderTable.getId());
  }

  @Test
  @DisplayName("list() : 모든 주문 테이블을 조회할 수 있다.")
  void test_list() throws Exception {
    //given
    final OrderTable orderTable = new OrderTable();

    final int beforeSize = tableService.list().size();

    orderTableDao.save(orderTable);

    //when
    final List<OrderTable> orderTables = tableService.list();

    //then
    assertEquals(orderTables.size(), beforeSize + 1);
  }

  @Test
  @DisplayName("changeEmpty() : 주문 테이블의 empty 상태를 변경할 수 있다.")
  void test_changeEmpty() throws Exception {
    //given
    final long orderTableId = 1L;
    final OrderTable orderTable = new OrderTable();
    orderTable.setEmpty(false);

    //when
    final OrderTable updatedOrderTable = tableService.changeEmpty(orderTableId, orderTable);

    //then
    assertEquals(orderTable.isEmpty(), updatedOrderTable.isEmpty());
  }

  @Test
  @DisplayName("changeEmpty() : 주문 테이블에 주문이 존재하고 그 상태가 COOKING이나 MEAL이면 empty를 변경할 수 없다.")
  void test_changeEmpty_IllegalArgumentException() throws Exception {
    final long orderTableId = 2L;
    final OrderTable orderTable = new OrderTable();
    orderTable.setEmpty(false);

    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("changeNumberOfGuests() : 주문 테이블의 인원 수를 수정할 수 있다.")
  void test_changeNumberOfGuests() throws Exception {
    //given
    final long orderTableId = 9L;
    final OrderTable orderTable = new OrderTable();
    orderTable.setNumberOfGuests(15);

    //when
    final OrderTable updatedOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

    //then
    assertEquals(updatedOrderTable.getNumberOfGuests(), orderTable.getNumberOfGuests());
  }

  @Test
  @DisplayName("changeNumberOfGuests() : 주문 테이블의 인원 수를 수정할 수 있다.")
  void test_changeNumberOfGuests_IllegalArgumentException() throws Exception {
    //given
    final long orderTableId = 1L;
    final OrderTable orderTable = new OrderTable();
    orderTable.setNumberOfGuests(15);

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
