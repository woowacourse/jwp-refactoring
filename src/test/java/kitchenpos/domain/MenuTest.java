package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴_생성() {
        final MenuProduct menuProduct = new MenuProduct(null, Product.of("치킨", BigDecimal.valueOf(10_000L)), 1L);

        assertDoesNotThrow(
                () -> Menu.of("치킨", BigDecimal.valueOf(10_000L), null, List.of(menuProduct))
        );
    }

    @ParameterizedTest
    @NullSource
    void 메뉴_생성_시_가격이_NULL이면_예외_발생(final BigDecimal price) {
        assertThatThrownBy(
                () -> Menu.of("치킨", price, null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 NULL일 수 없습니다. price: null");
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -10L, -10000L})
    void 메뉴_생성_시_가격이_음수면_예외_발생(final Long price) {
        assertThatThrownBy(
                () -> Menu.of("치킨", BigDecimal.valueOf(price), null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 음수일 수 없습니다. price: ");
    }

    @Test
    void 메뉴의_가격이_더_크면_예외_발생() {
        final MenuProduct menuProduct = new MenuProduct(null, Product.of("치킨", BigDecimal.valueOf(9_000L)), 1L);

        assertThatThrownBy(
                () -> Menu.of("치킨", BigDecimal.valueOf(10_000L), null, List.of(menuProduct))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 메뉴 상품 가격의 합보다 클 수 없습니다.");
    }
}
