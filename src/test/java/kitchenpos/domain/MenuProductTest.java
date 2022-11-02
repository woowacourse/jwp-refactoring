package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuProductTest {

    @Test
    void 금액을_계산할때_상품_ID가_같으면_금액과_개수를_곱한_값이_반환된다() {
        // given
        final Long productId = 1L;
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1L);
        final Product product = new Product();
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(100));

        // when
        final BigDecimal actual = menuProduct.calculateAmount(product);

        // then
        assertThat(actual).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    void 금액을_계산할때_상품_ID가_다르면_0을_반환한다() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        final Product product = new Product();
        product.setId(2L);

        // when
        final BigDecimal actual = menuProduct.calculateAmount(product);

        // then
        assertThat(actual).isEqualTo(BigDecimal.valueOf(0));
    }
}
