package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;

class MenuProductTest {
    @Test
    void 금액을_계산한다() {
        BigDecimal price = new BigDecimal(10000);
        Product product = new Product("상품", price);
        int quantity = 3;
        MenuProduct menuProduct = new MenuProduct(product, quantity);

        assertThat(menuProduct.calculateAmount().compareTo(price.multiply(BigDecimal.valueOf(quantity)))).isZero();
    }
}
