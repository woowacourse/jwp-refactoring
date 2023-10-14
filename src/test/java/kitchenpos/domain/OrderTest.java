package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문의_상태를_변경할_때_이미_완료된_주문이라면_예외를_던진다() {
        // given
        Order order = new Order(1L, COMPLETION, LocalDateTime.now());

        // expect
        assertThatThrownBy(() -> order.changeOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
    }
}
