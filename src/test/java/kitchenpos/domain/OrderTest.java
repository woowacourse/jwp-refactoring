package kitchenpos.domain;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class OrderTest {

    @Test
    @DisplayName("식사가 끝났으면 상태를 변경할 수 없다.")
    void changeOrderStatusTest() {

        // given
        Order order = new Order(1L, null, OrderStatus.COMPLETION, LocalDateTime.now(), null);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> order.changeStatus(OrderStatus.COOKING);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable);
    }
}
