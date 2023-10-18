package kitchenpos.application.product;

import kitchenpos.application.ProductService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;


class ProductServiceTest extends ApplicationTestConfig {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("[SUCCESS] 상품을 생성한다.")
    @Test
    void success_create() {
        // given
        final Product expected = new Product("테스트용 상품 이름", new Price("10000"));

        // when
        final Product actual = productService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        });
    }
}
