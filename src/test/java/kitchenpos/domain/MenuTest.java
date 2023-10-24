package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴_생성() {
        assertDoesNotThrow(
                () -> Menu.of("치킨", 10_000L, null)
        );
    }

    @ParameterizedTest
    @NullSource
    void 메뉴_생성_시_가격이_NULL이면_예외_발생(final Long price) {
        assertThatThrownBy(
                () -> Menu.of("치킨", price, null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 null일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -10L, -10000L})
    void 메뉴_생성_시_가격이_음수면_예외_발생(final Long price) {
        assertThatThrownBy(
                () -> Menu.of("치킨", price, null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 음수일 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource({"100,true", "15000, false"})
    void 메뉴의_가격이_더_크면_TRUE_반환(final BigDecimal price, final boolean result) {
        final Menu menu = Menu.of("치킨", 10_000L, null);

        assertThat(menu.isGreaterThan(price)).isEqualTo(result);
    }
}
