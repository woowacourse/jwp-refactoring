package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "-999"})
    void 메뉴의_가격이_null_이거나_0보다_작으면_예외가_발생한다(final BigDecimal value) {
        // given
        final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
        final var menuProducts = Collections.<MenuProduct>emptyList();

        // when & then
        assertThatThrownBy(() -> new Menu("망고 치킨", value, menuGroup, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격과_메뉴_상품의_총_가격이_다르면_예외가_발생한다() {
        // given
        final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
        final var menuProduct1 = new MenuProduct(ProductFixture.상품_망고_1000원(), 1L);
        final var menuProduct2 = new MenuProduct(ProductFixture.상품_치킨_15000원(), 1L);
        final var menuProducts = List.of(menuProduct1, menuProduct2);

        // when & then
        final BigDecimal price = BigDecimal.valueOf(1_000_000);
        assertThatThrownBy(() -> new Menu("망고 치킨", price, menuGroup, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
