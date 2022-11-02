package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.ui.jpa.dto.product.ProductCreateRequest;
import kitchenpos.ui.jpa.dto.product.ProductCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("product를 생성한다.")
    @Test
    void create() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("이름", 1000L);

        ProductCreateResponse productCreateResponse = productService.create(productCreateRequest);
        assertThat(productCreateResponse.getId()).isNotNull();
        assertThat(productCreateResponse.getName()).isEqualTo(productCreateRequest.getName());
        assertThat(productCreateResponse.getPrice()).isEqualTo(productCreateRequest.getPrice());
    }

    @DisplayName("product를 모두 조회한다.")
    @Test
    void list() {
        int resultSizeBeforeCreate = productService.list().size();
        productService.create(new ProductCreateRequest("이름", 1000L));

        int resultSize = productService.list().size();

        assertThat(resultSizeBeforeCreate + 1).isEqualTo(resultSize);
    }
}
