package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.vo.PriceIsNegativeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴_가격이_0원_미만_이라면_예외를_던진다() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1L);

        // when, then
        assertThatThrownBy(() -> new Menu(null, "name", price))
                .isInstanceOf(PriceIsNegativeException.class);
    }

    @Test
    void 메뉴_가격이_0원_이상_이라면_정상_생성_된다() {
        // given
        BigDecimal price = BigDecimal.valueOf(1L);

        // when, then
        assertThatCode(() -> new Menu(null, "name", price))
                .doesNotThrowAnyException();
    }
}
