package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.Constants.TEST_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductServiceTest extends KitchenPosServiceTest {

    @DisplayName("Product 생성 - 성공, 올바른 Price일 때")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void create_CorrectPrice_Success(int price) {
        BigDecimal productPrice = BigDecimal.valueOf(price);

        ProductRequest productRequest = new ProductRequest(TEST_PRODUCT_NAME, productPrice);

        ProductResponse createdProduct = productService.create(productRequest);

        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo(TEST_PRODUCT_NAME);
        assertThat(createdProduct.getPrice()).isEqualByComparingTo(productPrice);
    }

    @DisplayName("Product 생성 - 실패, 올바르지 않은 Price일 때")
    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-2, -1})
    void create_IncorrectPrice_ThrownException(Integer price) {
        BigDecimal productPrice = null;
        if (Objects.nonNull(price)) {
            productPrice = BigDecimal.valueOf(price);
        }

        ProductRequest productRequest = new ProductRequest(TEST_PRODUCT_NAME, productPrice);

        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 Product 조회 - 성공")
    @Test
    void list_Success() {
        ProductRequest productRequest = new ProductRequest(TEST_PRODUCT_NAME, TEST_PRODUCT_PRICE);
        ProductResponse createdProduct = productService.create(productRequest);

        List<ProductResponse> products = productService.list();

        assertThat(products).isNotNull();
        assertThat(products).isNotEmpty();
        assertThat(products).contains(createdProduct);
    }
}
