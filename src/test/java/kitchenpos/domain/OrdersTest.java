package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrdersTest {
    @DisplayName("주문을 생성한다. - 실패, 주문 테이블이 비어있음")
    @Test
    void createFailed() {
        // given - when
        OrderTable orderTable = new OrderTable(0, true);
        assertThatThrownBy(() -> new Orders(orderTable))
                .isInstanceOf(FieldNotValidException.class);
    }

}
