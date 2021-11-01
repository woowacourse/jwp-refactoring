package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("상품 관리 서비스 테스트")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        long price = 1000L;
        ProductRequest product = new ProductRequest("productName", price);
        ProductResponse created = productService.create(product);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo(product.getName()),
            () -> assertThat(created.getPrice().intValue()).isEqualTo(price)
        );
    }

    @DisplayName("상품 목록을 불러온다.")
    @Test
    void list() {
        ProductRequest product = new ProductRequest("productName1", 1000L);
        productService.create(product);
        productService.create(product);

        List<ProductResponse> products = productService.list();
        assertThat(products.size()).isEqualTo(2);
    }
}