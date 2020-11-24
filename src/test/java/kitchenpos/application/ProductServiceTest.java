package kitchenpos.application;

import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.exception.InvalidProductPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("새로운 상품 생성")
    @Test
    void createProductTest() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("양념치킨", BigDecimal.valueOf(16_000));

        ProductResponse productResponse = this.productService.createProduct(productCreateRequest);

        assertAll(
                () -> assertThat(productResponse).isNotNull(),
                () -> assertThat(productResponse.getName()).isEqualTo(productCreateRequest.getName()),
                () -> assertThat(productResponse.getPrice()).isEqualTo(productCreateRequest.getPrice())
        );
    }

    @DisplayName("새로운 상품을 생성할 때 가격이 존재하지 않으면 예외 발생")
    @Test
    void createProductWithNullPriceThenThrowException() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("양념치킨", null);

        assertThatThrownBy(() -> this.productService.createProduct(productCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 상품을 생성할 때 가격이 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -9999})
    void createProductWithInvalidPriceThenThrowException(int invalidPrice) {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("양념치킨", BigDecimal.valueOf(invalidPrice));

        assertThatThrownBy(() -> this.productService.createProduct(productCreateRequest)).isInstanceOf(InvalidProductPriceException.class);
    }

    @DisplayName("존재하는 모든 상품을 조회")
    @Test
    void listProductTest() {
        ProductCreateRequest productCreateRequest1 = new ProductCreateRequest("양념치킨", BigDecimal.valueOf(16_000));
        ProductCreateRequest productCreateRequest2 = new ProductCreateRequest("간장치킨", BigDecimal.valueOf(16_000));

        List<ProductCreateRequest> productCreateRequests = Arrays.asList(productCreateRequest1, productCreateRequest2);
        productCreateRequests.forEach(product -> this.productService.createProduct(product));

        List<ProductResponse> productResponses = this.productService.listAllProducts();

        assertThat(productResponses).hasSize(productCreateRequests.size());
    }
}