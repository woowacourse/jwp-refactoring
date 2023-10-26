package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuProductFixture.메뉴_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.vo.Price;
import kitchenpos.menu.vo.ProductSpecification;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductsTest {

    @Test
    void 정상_생성된다() {
        // given
        Long productId = 1L;
        long quantity = 1L;
        ProductSpecification product = ProductSpecification.from("productName", BigDecimal.ONE);

        List<MenuProduct> menuProducts = List.of(메뉴_상품(productId, quantity, product));

        // expect
        assertThatNoException().isThrownBy(() -> MenuProducts.from(menuProducts));
    }

    @Test
    void 메뉴_상품의_총_가격을_계산한다() {
        // given
        Long productId = 1L;
        long quantity = 1L;
        ProductSpecification product = ProductSpecification.from("productName", BigDecimal.ONE);
        List<MenuProduct> menuProducts = List.of(메뉴_상품(productId, quantity, product));

        // when
        MenuProducts products = MenuProducts.from(menuProducts);

        // then
        assertThat(products.calculateMenuProductsTotalPrice()).isEqualTo(Price.valueOf(BigDecimal.ONE));
    }
}
