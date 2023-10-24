package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    void 메뉴상품의_총액을_계산한다() {
        BigDecimal veryExpensivePrice = BigDecimal.valueOf(999999999999999999L);
        MenuProduct kongHana = new MenuProduct(new Product("kong hana", veryExpensivePrice), 1);

        assertThat(kongHana.calculatePrice()).isEqualTo(veryExpensivePrice);
    }

    @Test
    void 상품이_여러_개_일때_총액을_계산한다() {
        BigDecimal price = BigDecimal.valueOf(123);
        long quantity = 3;

        MenuProduct chicken = new MenuProduct(new Product("chicken", price), quantity);

        assertThat(chicken.calculatePrice()).isEqualTo(price.multiply(BigDecimal.valueOf(3)));

    }
}
