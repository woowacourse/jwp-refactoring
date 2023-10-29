package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuValidatorTest extends ServiceTest {

    @Autowired
    private MenuValidator menuValidator;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "-999"})
    void 메뉴의_가격이_null_이거나_0보다_작으면_예외가_발생한다(final BigDecimal value) {
        // given
        final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
        단일_메뉴그룹_저장(menuGroup);

        final var menuProducts = Collections.<MenuProduct>emptyList();
        final var menu = new Menu("망고 치킨", value, menuGroup.getId(), menuProducts);

        // when & then
        Assertions.assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격과_메뉴_상품의_총_가격이_다르면_예외가_발생한다() {
        // given
        final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
        단일_메뉴그룹_저장(menuGroup);

        final var product1 = ProductFixture.상품_망고_1000원();
        final var product2 = ProductFixture.상품_치킨_15000원();
        복수_상품_저장(product1, product2);

        final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
        final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
        final var menuProducts = List.of(menuProduct1, menuProduct2);
        final var price = BigDecimal.valueOf(1_000_000);
        final var menu = new Menu("망고 치킨", price, menuGroup.getId(), menuProducts);

        // when & then
        Assertions.assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
