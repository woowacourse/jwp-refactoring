package kitchenpos.table.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

  @Test
  @DisplayName("extractMenuIds()를 호출하면 메뉴 id의 리스트를 얻을 수 있다.")
  void extractMenuIds() {
    //given
    final OrderTables orderTables = new OrderTables(
        List.of(
            new OrderTable(1L, 1L),
            new OrderTable(3L, 2L),
            new OrderTable(2L, 1L),
            new OrderTable(4L, 3L)
        )
    );
    final List<Long> expected = List.of(1L, 3L, 2L, 4L);

    //when
    final List<Long> actual = orderTables.extractOrderTableIds();

    //then
    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  @DisplayName("isEmpty()를 호출하면 OrderTables의 리스트가 비어있는지 여부를 알 수 있다.")
  void isEmpty() {
    //given
    final OrderTables orderTables = new OrderTables(List.of());

    //when
    boolean actual = orderTables.isEmpty();

    //then
    Assertions.assertThat(actual).isTrue();
  }

  @Test
  @DisplayName("isDifferentSize()를 호출하면 OrderTables의 크기가 입력받은 값과 다른지 여부를 알 수 있다.")
  void isDifferentSize() {
    //given
    final OrderTables orderTables = new OrderTables(
        List.of(
            new OrderTable(1L, 1L),
            new OrderTable(3L, 2L)
        )
    );

    //when
    boolean actual = orderTables.isDifferentSize(3);

    //then
    Assertions.assertThat(actual).isTrue();
  }

  @Nested
  class validateAllOrderTablesEmptyAndNotHaveTableGroup {

    @Test
    @DisplayName("OrderTables의 리스트에 비어있지 않은 테이블이 포함되어 있으면 예외를 반환한다.")
    void not_empty_OrderTable() {
      //given
      final OrderTables orderTables = new OrderTables(
          List.of(
              new OrderTable(0, true),
              new OrderTable(3, false)
          )
      );

      //when
      final ThrowingCallable actual = () -> orderTables.validateAllOrderTablesEmptyAndNotHaveTableGroup();

      //then
      Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTables의 리스트에 테이블 그룹에 속한 주문 테이블이 포함되어 있으면 예외를 반환한다.")
    void have_TableGroup() {
      //given
      final OrderTables orderTables = new OrderTables(
          List.of(
              new OrderTable(1L, null),
              new OrderTable(2L, 1L)
          )
      );

      //when
      final ThrowingCallable actual = () -> orderTables.validateAllOrderTablesEmptyAndNotHaveTableGroup();

      //then
      Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Test
  @DisplayName("ungroup을 호출하면 OrderTables의 모든 주문 테이블을 테이블 그룹에 속하지 않은 상태로 변경한 후 리스트로 반환한다.")
  void ungroup() {
    //given
    final OrderTables orderTables = new OrderTables(
        List.of(
            new OrderTable(1L, 1L, 2, false),
            new OrderTable(3L, 2L, 0, true),
            new OrderTable(2L, 1L, 3, true),
            new OrderTable(4L, 3L, 4, false)
        )
    );
    final List<OrderTable> expected = List.of(
        new OrderTable(1L, null, 2, false),
        new OrderTable(3L, null, 0, false),
        new OrderTable(2L, null, 3, false),
        new OrderTable(4L, null, 4, false)
    );

    //when
    final List<OrderTable> actual = orderTables.ungroup();

    //then
    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

}