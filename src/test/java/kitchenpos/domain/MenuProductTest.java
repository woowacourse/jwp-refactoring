package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("상품 개수에 따른 가격 계산")
    @Test
    void calculatePrice() {
        Product product = createProduct(15_000);
        Menu menu = createMenu(10);
        MenuProduct menuProduct = createMenuProduct(menu, product, 2);

        BigDecimal actual = menuProduct.calculatePrice();
        BigDecimal expected = BigDecimal.valueOf(30_000);
        assertThat(actual).isEqualTo(expected);
    }
}