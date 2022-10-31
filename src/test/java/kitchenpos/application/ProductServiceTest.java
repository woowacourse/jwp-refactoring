package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    private final ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @Test
    void createProduct() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("상품", BigDecimal.valueOf(1000));

        // when
        ProductResponse savedProduct = productService.create(request);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void createProductWithNullPrice() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("상품", null);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createProductWithZeroPrice() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("상품", BigDecimal.valueOf(0));

        // when
        ProductResponse response = productService.create(request);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void createProductWithNegativePrice() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("상품", BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findProducts() {
        // given & when
        List<ProductResponse> responses = productService.list();

        // then
        int defaultSize = 6;
        assertThat(responses).hasSize(defaultSize);
    }
}
