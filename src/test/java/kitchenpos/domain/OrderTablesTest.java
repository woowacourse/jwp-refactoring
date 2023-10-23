package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTablesTest {

  @ParameterizedTest
  @MethodSource("aa")
  @DisplayName("validateNotEmptyOrNotBelongTableGroup() : 주문 테이블이 비어있거나 주문 테이블이 테이블 그룹에 속해있지 않으면 IllegalArgumentException를 반환할 수 있다.")
  void test_validateNotEmptyOrNotBelongTableGroup_IllegalArgumentException(
      final OrderTables orderTables
  ) throws Exception {
    //when & then
    assertThatThrownBy(() -> orderTables.validateNotEmptyOrNotBelongTableGroup())
        .isInstanceOf(IllegalArgumentException.class);
  }

  static Stream aa() {
    //주문 테이블이 비어있고, 주문 테이블이 다른 테이블 그룹에 속해있는 경우
    final OrderTable orderTable1 = new OrderTable(1L, 2L, 3, true);

    //주문 테이블이 비어있고, 주문 테이블이 다른 테이블 그룹에 속해있지 않은 경우
    final OrderTable orderTable2 = new OrderTable(1L, null, 3, true);

    //주문 테이블이 비어있지 않고, 주문 테이블이 다른 테이블 그룹에 속해있지 않는 경우
    final OrderTable orderTable3 = new OrderTable(1L, null, 3, true);

    return Stream.of(
        new OrderTables(List.of(orderTable1, orderTable1, orderTable1)),
        new OrderTables(List.of(orderTable2,orderTable2,orderTable2)),
        new OrderTables(List.of(orderTable3,orderTable3,orderTable3))
    );
  }
}
