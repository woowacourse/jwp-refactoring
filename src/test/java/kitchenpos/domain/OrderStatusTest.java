package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

  @Test
  @DisplayName("isCompletion() : 현재 status가 COMPLETION이면 true를 반환할 수 있다.")
  void test_isCompletion() throws Exception {
    //given
    final OrderStatus orderStatus = OrderStatus.COMPLETION;

    //when & then
    assertTrue(orderStatus.isCompletion());
  }
}
