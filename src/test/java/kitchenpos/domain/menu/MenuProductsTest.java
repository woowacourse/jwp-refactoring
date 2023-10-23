package kitchenpos.domain.menu;

import kitchenpos.domain.DomainTest;
import kitchenpos.domain.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductsTest extends DomainTest {
    @Test
    void create_menu_products() {
        // given
        final List<Product> products = List.of(Product.of("후라이드", 16_000L), Product.of("양념치킨", 16_000L));
        final List<Long> quantities = List.of(1L, 1L);

        // when
        final MenuProducts menuProducts = MenuProducts.from(products, quantities);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(menuProducts).isNotNull();
            softly.assertThat(menuProducts.getItems()).isNotNull();
            softly.assertThat(menuProducts.getItems()).hasSize(2);
        });
    }

    @Test
    void throw_when_products_size_and_quantities_size_are_different() {
        // given
        final List<Product> products = List.of(Product.of("후라이드", 16_000L), Product.of("양념치킨", 16_000L));
        final List<Long> quantities = List.of(1L);

        // when & then
        assertThatThrownBy(() -> MenuProducts.from(products, quantities))
                .isInstanceOf(IllegalArgumentException.class);
    }

}