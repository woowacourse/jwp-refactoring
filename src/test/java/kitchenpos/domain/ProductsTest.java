package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductsTest {

    @Test
    void 상품_총_금액을_계산한다() {
        // given
        final Long productId = 1L;
        final Product product = new Product();
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(100));
        final Products products = new Products(List.of(product));

        // when
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1L);
        final BigDecimal actual = products.calculateAmount(List.of(menuProduct));

        // then
        assertThat(actual).isEqualTo(BigDecimal.valueOf(100));
    }
}
