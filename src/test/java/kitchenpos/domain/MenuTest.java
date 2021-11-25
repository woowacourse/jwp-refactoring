package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuTest {

    @Test
    @DisplayName("가격은 0 이상이어야 한다.")
    void validatePriceTest() {

        // given
        Menu menu = new Menu(1L, "이름", BigDecimal.ZERO, null, new ArrayList<>());

        // when
        ThrowableAssert.ThrowingCallable callable = menu::validatePrice;

        // then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가격은 0 이상이어야 한다. 실패")
    void validatePriceFailTest() {

        // given
        Menu menu = new Menu(1L, "이름", BigDecimal.valueOf(-1), null, new ArrayList<>());

        // when
        ThrowableAssert.ThrowingCallable callable = menu::validatePrice;

        // then
        assertThatIllegalArgumentException().isThrownBy(callable);
    }
}
