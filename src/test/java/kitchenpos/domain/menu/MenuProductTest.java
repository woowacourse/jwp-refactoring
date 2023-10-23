package kitchenpos.domain.menu;

import kitchenpos.domain.DomainTest;
import kitchenpos.domain.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuProductTest extends DomainTest {
    @Test
    void throw_when_quantity_is_negative() {
        // given
        final Product product = Product.of("후라이드", 16_000L);
        final long quantity = -1L;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> MenuProduct.of(product, quantity));
    }

    @Test
    void throw_when_quantity_is_zero() {
        // given
        final Product product = Product.of("후라이드", 16_000L);
        final long quantity = 0L;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> MenuProduct.of(product, quantity));
    }

    @Test
    void create_menu_product() {
        // given
        final Product product = Product.of("후라이드", 16_000L);
        final long quantity = 1L;

        // when
        final MenuProduct menuProduct = MenuProduct.of(product, quantity);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(menuProduct.getProduct()).isEqualTo(product);
            softAssertions.assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
        });
    }

}