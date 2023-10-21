package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName("changeStatus() : 주문 상태가 Completion이 아니면 주문 상태를 변경할 수 있다.")
  void test_changeStatus() throws Exception {
    //given
    final Order order = OrderFixture.createCookingOrder(
        OrderTableFixture.createEmptySingleOrderTable()
    );

    final OrderStatus actual = OrderStatus.MEAL;
    //when
    order.changeStatus(actual);

    //then
    assertEquals(order.getOrderStatus(), actual);
  }

  @Test
  @DisplayName("changeStatus() : 주문 상태가 Comletion일 때, 주문 상태를 변경하면 IllegalArgumentException가 발생할 수 있다.")
  void test_changeStatus_IllegalArgumentException() throws Exception {
    //given
    final Order order = OrderFixture.createCompletionOrder(
        OrderTableFixture.createEmptySingleOrderTable()
    );

    //when & then
    assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
