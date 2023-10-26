package kitchenpos.domain.menu;

import kitchenpos.domain.DomainTest;
import kitchenpos.domain.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest extends DomainTest {
    @Test
    void create_menu() {
        //given
        MenuGroup menuGroup = MenuGroup.of("후라이드");
        final List<Product> products = List.of(
                Product.of("후라이드", 16_000L),
                Product.of("양념치킨", 16_000L)
        );
        final List<Long> quantities = List.of(1L, 1L);

        //when
        final Menu actual = Menu.of("후라이드", 16_000L, menuGroup, MenuProducts.from(products, quantities));

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual).isNotNull();
            softly.assertThat(actual.getName().getName()).isEqualTo("후라이드");
            softly.assertThat(actual.getPrice().getPrice()).isEqualTo(BigDecimal.valueOf(16_000L));
            softly.assertThat(actual.getMenuGroup())
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(menuGroup);
            softly.assertThat(actual.getMenuProducts()).isNotNull();
            softly.assertThat(actual.getMenuProducts().getItems()).hasSize(2);
        });
    }

    @Test
    void throw_when_menu_price_is_bigger_than_sum_of_product_price() {
        //given
        MenuGroup menuGroup = MenuGroup.of("후라이드");
        final List<Product> products = List.of(
                Product.of("후라이드", 16_000L),
                Product.of("양념치킨", 16_000L)
        );
        final List<Long> quantities = List.of(1L, 1L);

        //when & then
        assertThatThrownBy(() -> Menu.of("후라이드", 32_000L, menuGroup, MenuProducts.from(products, quantities)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}