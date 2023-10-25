package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("총 가격을 구한다.")
    void totalPrice() {
        // given
        final MenuProduct menuProduct = new MenuProduct(new Product("name", BigDecimal.TEN), 3);

        // when
        final BigDecimal totalPrice = menuProduct.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(30));
    }
}
