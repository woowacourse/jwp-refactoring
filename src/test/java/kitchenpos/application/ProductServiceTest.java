package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Product;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        Product product = new Product();
        product.setName("고추마요치킨");
        product.setPrice(BigDecimal.valueOf(18_000));

        Product saved = productService.create(product);

        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("상품 가격이 음수 혹은 null이면 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = "-1000")
    @NullSource
    void create_GivenMinus_ThenThrownException(BigDecimal price) {
        Product product = new Product();
        product.setName("고추마요치킨");
        product.setPrice(price);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        Product product = new Product();
        product.setName("고추마요치킨");
        product.setPrice(BigDecimal.valueOf(18_000));
        productService.create(product);

        List<Product> actual = productService.list();

        assertThat(actual).isNotEmpty();
    }
}