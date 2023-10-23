package kitchenpos.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 완료된_주문의_상태를_변경하려는_경우_예외가_발생한다() {
        // given
        final Order order = new Order(null, OrderStatus.COMPLETION, null);

        // expect
        assertThatThrownBy(() -> order.updateStatus("COOKING"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 완료된_주문이_아니라면_예외를_발생시킬_수_있다(final String status) {
        // given
        final Order order = new Order(null, OrderStatus.valueOf(status), null);

        // expect
        assertThatThrownBy(order::validateUncompleted)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료되지 않은 주문입니다");
    }

}
