package kitchenpos.application;

import kitchenpos.TestDomainFactory;
import kitchenpos.domain.Product;
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
        Product product = TestDomainFactory.createProduct("양념치킨", BigDecimal.valueOf(16_000));

        Product savedProduct = this.productService.create(product);

        assertAll(
                () -> assertThat(savedProduct).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice().toBigInteger()).isEqualTo(product.getPrice().toBigInteger())
        );
    }

    @DisplayName("새로운 상품을 생성할 때 가격이 존재하지 않으면 예외 발생")
    @Test
    void createProductWithNullPriceThenThrowException() {
        Product product = TestDomainFactory.createProduct("양념치킨", null);

        assertThatThrownBy(() -> this.productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 상품을 생성할 때 가격이 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -9999})
    void createProductWithInvalidPriceThenThrowException(int invalidPrice) {
        Product product = TestDomainFactory.createProduct("양념치킨", BigDecimal.valueOf(invalidPrice));

        assertThatThrownBy(() -> this.productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 상품을 조회")
    @Test
    void listProductTest() {
        Product product1 = TestDomainFactory.createProduct("양념치킨", BigDecimal.valueOf(16_000));
        Product product2 = TestDomainFactory.createProduct("간장치킨", BigDecimal.valueOf(16_000));

        List<Product> products = Arrays.asList(product1, product2);
        products.forEach(product -> this.productService.create(product));

        List<Product> savedProducts = this.productService.list();

        assertAll(
                () -> assertThat(savedProducts.size()).isEqualTo(products.size()),
                () -> assertThat(savedProducts.get(0).getName()).isEqualTo(product1.getName()),
                () -> assertThat(savedProducts.get(1).getName()).isEqualTo(product2.getName())
        );
    }
}