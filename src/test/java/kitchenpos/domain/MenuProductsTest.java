package kitchenpos.domain;

import static kitchenpos.common.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.common.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.vo.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductsTest {

    @Test
    void 정상_생성된다() {
        // given
        List<MenuProduct> menuProducts = List.of(메뉴_상품());

        // expect
        assertThatNoException().isThrownBy(() -> MenuProducts.from(menuProducts));
    }

    @Test
    void 메뉴_상품의_총_가격을_계산한다() {
        // given
        Product product = 상품(BigDecimal.ONE);
        long quantity = 1L;
        List<MenuProduct> menuProducts = List.of(메뉴_상품(product, quantity));

        // when
        MenuProducts products = MenuProducts.from(menuProducts);

        // then
        assertThat(products.calculateMenuProductsTotalPrice()).isEqualTo(Money.valueOf(BigDecimal.ONE));
    }
}
