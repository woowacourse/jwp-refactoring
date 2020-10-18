package kitchenpos.application;

import kitchenpos.config.IsolatedTest;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import kitchenpos.ui.dto.product.ProductResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends IsolatedTest {

    @Autowired
    private ProductService service;

    @DisplayName("상품 생성")
    @Test
    public void createProduct() {
        ProductRequest request = new ProductRequest("포테이토 피자", BigDecimal.valueOf(12_000L));
        final ProductResponse response = service.create(request);

        assertThat(response.getName()).isEqualTo("포테이토 피자");
        assertThat(response.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(12_000L));
    }

    @DisplayName("상품 조회")
    @Test
    public void readProducts() {
        ProductRequest request = new ProductRequest("포테이토 피자", BigDecimal.valueOf(12_000L));
        final ProductResponse response = service.create(request);

        final ProductResponses responses = service.list();

        assertThat(responses.getProductResponses()).hasSize(1);
        assertThat(responses.getProductResponses().get(0).getName()).isEqualTo("포테이토 피자");
        assertThat(responses.getProductResponses().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(12_000L));
    }
}
