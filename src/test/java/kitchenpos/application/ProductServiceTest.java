package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTestContext {

    @Test
    void 상품_생성_시_가격이_0보다_작으면_예외를_던진다() {
        // given
        Product product = new Product();
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성_시_가격을_지정하지_않았다면_예외를_던진다() {
        // given
        Product product = new Product();
        product.setName("name");

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품을_정상적으로_생성하는_경우_생성한_상품이_반환된다() {
        // given
        Product product = new Product();
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(1000L));

        // when
        Product createdProduct = productService.create(product);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createdProduct.getId()).isNotNull();
            softly.assertThat(createdProduct.getName()).isEqualTo(product.getName());
        });
    }

    @Test
    void 전체_상품을_조회할_수_있다() {
        // given
        Product product = new Product();
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(1000L));
        productService.create(product);

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }
}
