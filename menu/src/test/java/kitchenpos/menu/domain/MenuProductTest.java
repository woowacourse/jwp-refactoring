package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductTest {
    @Test
    void throw_when_quantity_is_negative() {
        // given
        final Product product = Product.of("후라이드", 16_000L);
        final long quantity = -1L;

        // when & then
        assertThatThrownBy(() -> MenuProduct.of(product, Quantity.of(quantity)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Quantity.PRODUCT_QUANTITY_IS_UNDER_ONE_ERROR_MESSAGE);
    }

    @Test
    void throw_when_quantity_is_zero() {
        // given
        final Product product = Product.of("후라이드", 16_000L);
        final long quantity = 0L;

        // when & then
        assertThatThrownBy(() -> MenuProduct.of(product, Quantity.of(quantity)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Quantity.PRODUCT_QUANTITY_IS_UNDER_ONE_ERROR_MESSAGE);
    }

    @Test
    void create_menu_product() {
        // given
        final Product product = Product.of("후라이드", 16_000L);
        final long quantity = 1L;

        // when
        final MenuProduct menuProduct = MenuProduct.of(product, Quantity.of(quantity));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(menuProduct.getProduct()).isEqualTo(product);
            softAssertions.assertThat(menuProduct.getQuantity().getQuantity()).isEqualTo(quantity);
        });
    }

}