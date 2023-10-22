package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName("updateOrderStatus()를 호출하면 주문의 상태를 변경할 수 있다.")
  void updateOrderStatus() {
    //given
    final OrderStatus before = OrderStatus.COOKING;
    final OrderStatus after = OrderStatus.MEAL;
    final Order order = new Order(null, null, before, null);
    
    //when
    order.updateOrderStatus(after);

    //then
    Assertions.assertThat(order.getOrderStatus()).isEqualTo(after);

  }

}