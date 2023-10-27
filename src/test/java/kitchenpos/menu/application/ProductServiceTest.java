package kitchenpos.menu.application;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.application.request.ProductCreateRequest;
import kitchenpos.menu.application.response.ProductResponse;
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
        final ProductCreateRequest expected = new ProductCreateRequest("테스트용 상품명", "10000");

        // when
        final ProductResponse actual = productService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        });
    }
}
