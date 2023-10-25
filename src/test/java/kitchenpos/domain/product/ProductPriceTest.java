package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class ProductPriceTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1"})
    void 가격이_null_이거나_0보다_작다면_예외가_발생한다(BigDecimal invalidPrice) {
        // given
        // when
        final ThrowingCallable throwingCallable = () -> new ProductPrice(invalidPrice);

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 가격의_소수_부분이_2자리_이상일_경우_예외가_발생한다() {
        // given
        final var invalidPrice = BigDecimal.valueOf(100.001);

        // when
        final ThrowingCallable throwingCallable = () -> new ProductPrice(invalidPrice);

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격은 소수 부분은 2자리까지만 입력 가능합니다.");
    }
}
