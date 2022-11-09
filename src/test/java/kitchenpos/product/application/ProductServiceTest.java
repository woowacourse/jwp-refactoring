package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.product.application.response.ProductResponse;

public class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));

        // when
        ProductResponse savedProduct = productService.create(request);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(request.getName());
        assertPriceEqualTo(request.getPrice(), savedProduct.getPrice());
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void list() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        ProductResponse savedProduct = productService.create(request);

        // when
        List<ProductResponse> result = productService.list();

        // then
        assertProductResponse(result.get(0), savedProduct);
    }

    private void assertPriceEqualTo(final BigDecimal actual, final BigDecimal expected) {
        assertThat(actual.compareTo(expected)).isEqualTo(0);
    }

    private void assertProductResponse(final ProductResponse actual, final ProductResponse expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertPriceEqualTo(actual.getPrice(), expected.getPrice());
    }
}
