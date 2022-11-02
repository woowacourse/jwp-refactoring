package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.product.ui.request.ProductCreateRequest;
import kitchenpos.product.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        final String productName = "맥북m1";
        final BigDecimal productPrice = BigDecimal.valueOf(3000);
        final ProductCreateRequest request = new ProductCreateRequest(productName, productPrice);

        final ProductResponse response = productService.create(request);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo(productName),
                () -> assertThat(response.getPrice()).isEqualByComparingTo(productPrice)
        );
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void getProducts() {
        final List<ProductResponse> products = productService.list();

        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).getName()).isEqualTo("후라이드"),
                () -> assertThat(products.get(1).getName()).isEqualTo("양념치킨")
        );
    }
}
