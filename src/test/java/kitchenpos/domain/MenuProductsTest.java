package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    void 메뉴_상품의_가격을_구한다() {
        // given
        final Product product = new Product("양념 치킨", BigDecimal.valueOf(10000));
        final MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(product, 2)));

        // when
        final BigDecimal result = menuProducts.calculateMenuPrice();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(20000));
    }
}
