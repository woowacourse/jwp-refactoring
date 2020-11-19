package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {
    @DisplayName("ProductIds 추출")
    @Test
    void extractProductIds() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(1L, 1L, 1L, 1),
                new MenuProduct(1L, 1L, 2L, 1),
                new MenuProduct(1L, 1L, 3L, 1)));

        List<Long> expected = Arrays.asList(1L, 2L, 3L);

        assertThat(menuProducts.extractProductIds()).isEqualTo(expected);
    }

    @DisplayName("menu의 product * quantity 의 합 추출")
    @Test
    void calculateProductsPriceSum() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(1L, 1L, 1L, 1),
                new MenuProduct(1L, 1L, 2L, 1),
                new MenuProduct(1L, 1L, 3L, 3)));

        Products products = new Products(Arrays.asList(
                new Product(1L, "김", new Money(500L)),
                new Product(2L, "단무지", new Money(1000L)),
                new Product(3L, "햄", new Money(500L))
        ));

        Money expected = new Money(3000L);

        assertThat(menuProducts.calculateProductsPriceSum(products)).isEqualTo(expected);
    }
}