package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuPriceTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "-1.0", "-1.00"})
    void 메뉴_가격이_null이나_음수라면_에러가_발생한다(BigDecimal invalidPrice) {
        //given
        //when
        final ThrowingCallable action = () -> new MenuPrice(invalidPrice);

        //then
        assertThatThrownBy(action).isInstanceOf(InvalidPriceException.class)
                                  .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 메뉴_가격이_소수_3자리라면_에러가_발생한다() {
        //given
        final var invalidPrice = BigDecimal.valueOf(1.001);

        //when
        final ThrowingCallable action = () -> new MenuPrice(invalidPrice);

        //then
        assertThatThrownBy(action).isInstanceOf(InvalidPriceException.class)
                                  .hasMessage("가격은 소수 부분은 2자리까지만 입력 가능합니다.");
    }
}
