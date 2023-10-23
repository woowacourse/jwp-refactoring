package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuantityTest {

    @Test
    void 생성자는_유효한_수량을_전달하면_Quantity를_초기화한다() {
        // when & then
        assertThatCode(() -> new Quantity(1L)).doesNotThrowAnyException();
    }

    @Test
    void 생성자는_유효하지_않은_수량을_전달하면_예외가_발생한다() {
        // given
        final long invalidQuantity = -1L;

        // when & then
        assertThatThrownBy(() -> new Quantity(invalidQuantity))
                .isInstanceOf(InvalidQuantityException.class);
    }
}
