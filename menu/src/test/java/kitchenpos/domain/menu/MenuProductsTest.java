package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.money.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductsTest {

    @Test
    void 상품_금액의_총합을_반환한다() {
        // given
        MenuProduct menuProduct1 = MenuProductFixture.메뉴_상품(ProductFixture.상품("치즈 피자", 8900L), 2L);
        MenuProduct menuProduct2 = MenuProductFixture.메뉴_상품(ProductFixture.상품("포테이토 피자", 12900L), 3L);
        MenuProducts menuProducts = new MenuProducts(List.of(menuProduct1, menuProduct2));

        // expect
        assertThat(menuProducts.calculateAmount()).isEqualTo(Money.valueOf(56500L));
    }
}
