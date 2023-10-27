package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("단일 가격에 대해 개수만큼의 가격을 계산한다.")
    @Test
    void calculateMenuPrice() {
        // given
        final Product product = new Product("product", Price.from(BigDecimal.valueOf(500L)));
        final MenuProduct menuProduct = new MenuProduct(1L, 3);

        // when
        final Price calcultedMenuProductPrice = menuProduct.calculateMenuPrice(product.getPrice());

        // then
        assertThat(calcultedMenuProductPrice).isEqualTo(Price.from(BigDecimal.valueOf(1500L)));
    }
}
