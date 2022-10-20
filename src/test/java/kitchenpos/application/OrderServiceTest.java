package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.SpringServiceTest;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    @Nested
    class create_메소드는 {

        @Nested
        class 주문항목이_비어있는_경우 extends SpringServiceTest {

            private final Order order = new Order(1L, null, LocalDateTime.now(), new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문항목이 비어있습니다.");
            }
        }
    }
}
