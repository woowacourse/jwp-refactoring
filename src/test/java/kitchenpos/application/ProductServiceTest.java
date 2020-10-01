package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.Constants.TEST_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends KitchenPosServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("Product 생성 - 성공, 올바른 Price일 때")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void create_CorrectPrice_Success(int price) {
        BigDecimal productPrice = BigDecimal.valueOf(price);

        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(productPrice);

        Product createdProduct = productService.create(product);

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

        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(productPrice);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 Product 조회 - 성공")
    @Test
    void list_Succsss() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);
        Product createdProduct = productService.create(product);

        List<Product> products = productService.list();

        assertThat(products).isNotNull();
        assertThat(products).isNotEmpty();

        List<Long> productIds = products.stream()
            .map(Product::getId)
            .collect(Collectors.toList());

        assertThat(productIds).contains(createdProduct.getId());
    }
}
