package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceIntegrationTest {
    private static final BigDecimal INVALID_PRICE = BigDecimal.valueOf(-1);

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        // given
        final ProductCreateRequest product = new ProductCreateRequest("강정치킨", BigDecimal.valueOf(17000));

        // when
        final ProductResponse result = productService.create(product);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getName()).isEqualTo(product.getName());
            softly.assertThat(result.getPrice()).isEqualByComparingTo(product.getPrice());
        });
    }

    @Test
    void create_priceException() {
        // given
        final ProductCreateRequest product = new ProductCreateRequest("강정치킨", INVALID_PRICE);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final ProductResponse product1 = productService.create(
                new ProductCreateRequest("Product1", BigDecimal.valueOf(1000)));
        final ProductResponse product2 = productService.create(
                new ProductCreateRequest("Product2", BigDecimal.valueOf(2000)));

        // when
        final List<ProductResponse> result = productService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(2);
            softly.assertThat(result.get(0))
                    .usingRecursiveComparison()
                    .ignoringFields("price")
                    .isEqualTo(product1);
            softly.assertThat(result.get(0).getPrice()).isEqualByComparingTo(product1.getPrice());
            softly.assertThat(result.get(1))
                    .usingRecursiveComparison()
                    .ignoringFields("price")
                    .isEqualTo(product2);
            softly.assertThat(result.get(1).getPrice()).isEqualByComparingTo(product2.getPrice());
        });
    }
}
