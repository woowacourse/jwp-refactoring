package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("금액을 계산한다.")
    void calculateAmount() {
        // given
        final Product product = new Product("치킨", new BigDecimal("15000.00"));
        final MenuProduct menuProduct = new MenuProduct(null, product, 2);

        // when
        final BigDecimal amount = menuProduct.calculateAmount();

        // then
        assertThat(amount).isEqualTo("30000.00");
    }
}
