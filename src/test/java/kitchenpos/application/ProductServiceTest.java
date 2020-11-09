package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductServiceTest {

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
    @ValueSource(strings = {"null", "-1"})
    void createProductByInvalidInput(String value) {
        BigDecimal productPrice = value.equals("null") ? null : BigDecimal.valueOf(Long.valueOf(value));
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