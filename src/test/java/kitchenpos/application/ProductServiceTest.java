package kitchenpos.application;

import java.util.List;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("새로운 상품을 생성한다.")
    @Test
    void create_new_product() {
        // given
        final ProductRequest request = new ProductRequest("새 상품", 1000);

        // when
        final ProductResponse result = productService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(request.getName());
            softly.assertThat(result.getPrice()).isEqualTo(request.getPrice());
        });
    }

    @DisplayName("상품 전제 정보를 불러온다.")
    @Test
    void find_all_products() {
        // given
        final ProductRequest request1 = new ProductRequest("새 상품1", 2000);
        final ProductRequest request2 = new ProductRequest("새 상품2", 4000);
        productService.create(request1);
        productService.create(request2);

        // when
        final List<ProductResponse> result = productService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
