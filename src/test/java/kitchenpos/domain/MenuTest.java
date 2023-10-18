package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Test
    @DisplayName("메뉴에는 이름이 있어야 한다.")
    void 메뉴_생성_실패_이름_없음() {
        // given
        final String nullName = null;

        // expected
        assertThatThrownBy(() -> new Menu(
                nullName,
                BigDecimal.ONE,
                1L,
                List.of(new MenuProduct(1L, 1L, 1L, 1L))
        )).isInstanceOf(Exception.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 256})
    @DisplayName("메뉴 이름의 길이는 최소 1자, 최대 255자이다.")
    void 메뉴_생성_실패_이름_길이(final int nameLength) {
        // given
        final String name = "a".repeat(nameLength);

        // expected
        assertThatThrownBy(() -> new Menu(
                name,
                BigDecimal.ZERO,
                1L,
                List.of(new MenuProduct(1L, 1L, 1L, 1L))
        )).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("메뉴에는 가격이 있어야 한다.")
    void 메뉴_생성_실패_가격_없음() {
        // given
        final BigDecimal nullPrice = null;

        // expected
        assertThatThrownBy(() -> new Menu(
                "순두부 정식",
                nullPrice,
                1L,
                List.of(new MenuProduct(1L, 1L, 1L, 1L))
        )).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("메뉴 가격은 0 이상이어야 한다.")
    void 메뉴_생성_실패_가격_음수() {
        assertThatThrownBy(() -> new Menu(
                "순두부 정식",
                BigDecimal.valueOf(-1),
                1L,
                List.of(new MenuProduct(1L, 1L, 1L, 1L))
        )).isInstanceOf(Exception.class);
    }

    @Disabled
    @Test
    @DisplayName("메뉴는 1 종류 이상의 메뉴 상품으로 구성되어 있다.")
    void 메뉴_생성_실패_메뉴_상품_개수() {
        assertThatThrownBy(() -> new Menu(
                "순두부 정식",
                BigDecimal.ONE,
                1L,
                Collections.emptyList()
        )).isInstanceOf(Exception.class);
    }
}
