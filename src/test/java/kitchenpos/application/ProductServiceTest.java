package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Product;

class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"후라이드치킨,10000", "양념치킨,11000"})
    void createProductByValidInput(String name, Long price) {
        BigDecimal productPrice = BigDecimal.valueOf(price);
        Product productRequest = createProduct(null, name, productPrice);
        Product product = productService.create(productRequest);

        assertAll(
            () -> assertThat(product.getId()).isNotNull(),
            () -> assertThat(product.getName()).isEqualTo(name),
            () -> assertThat(product.getPrice().longValue()).isEqualTo(productPrice.longValue())
        );
    }

    @DisplayName("상품 가격은 음수이거나 null일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createProductByInvalidInput(String value) {
        BigDecimal productPrice = Objects.isNull(value) ? null : BigDecimal.valueOf(-1L);
        Product productRequest = createProduct(null, "후라이드치킨", productPrice);

        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void findAll() {
        Product productRequest1 = createProduct(null, "후라이드차킨", BigDecimal.valueOf(10000));
        Product productRequest2 = createProduct(null, "양념치킨", BigDecimal.valueOf(11000));

        Product friedChicken = productService.create(productRequest1);
        Product seasoningChicken = productService.create(productRequest2);

        List<Product> products = productService.list();

        assertThat(products).size().isEqualTo(2);
    }
}