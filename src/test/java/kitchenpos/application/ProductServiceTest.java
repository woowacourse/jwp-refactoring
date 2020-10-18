package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("create")
    void create() {
        Product product = new Product();
        product.setName("아무이름");
        product.setPrice(new BigDecimal(10_000));

        productService.create(product);
    }

    @Test
    @DisplayName("create - name 이 null 일 때 예외처리")
    void create_IfNameIsNull_ThrowException() {
        Product product = new Product();
        product.setPrice(new BigDecimal(1_000));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("create - price 가 null 일 때 예외처리")
    void create_IfPriceIsNull_ThrowException() {
        Product product = new Product();
        product.setName("아무이름");

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -9_000_000L})
    @DisplayName("create - price 가 음수일 때 예외처리")
    void create_IfPriceIsIllegal_ThrowException() {
        Product product = new Product();
        product.setName("아무이름");
        product.setPrice(new BigDecimal(-1_000));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        assertThat(productService.list()).hasSize(0);

        Product product = new Product();

        product.setName("아무이름1");
        product.setPrice(new BigDecimal(10_000));
        productService.create(product);

        product.setName("아무이름2");
        product.setPrice(new BigDecimal(20_000));
        productService.create(product);

        assertThat(productService.list()).hasSize(2);
    }
}
