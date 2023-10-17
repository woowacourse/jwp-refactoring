package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import javax.sql.DataSource;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;

@JdbcTest
class ProductServiceTest {

    private ProductService productService;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(new JdbcTemplateProductDao(dataSource));
    }

    @Test
    @DisplayName("이름과 가격을 제공하면 새로운 상품을 제공할 수 있다.")
    void givenNameAndPrice() {
        final Product product = new Product();
        product.setId(9987L);
        product.setName("쫀득쫀득 지렁이");
        product.setPrice(new BigDecimal("4000"));

        final Product savedProduct = this.productService.create(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId())
                .as("식별자는 주어진 값과 무관하게 할당받는다.")
                .isNotEqualTo(product.getId());
    }

    @Test
    @DisplayName("가격 정보는 비어있거나 음수의 값이 아니어야 한다.")
    void whenInvalidPrice() {
        final Product product = new Product();
        product.setName("쫀득쫀득 지렁이");

        product.setPrice(null);
        assertThatThrownBy(() -> productService.create(product))
                .as("가격이 비어있는 경우 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);

        product.setPrice(new BigDecimal("-1"));
        assertThatThrownBy(() -> productService.create(product))
                .as("가격이 음수인 경우에도 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 소수점 2자리를 포함해 총 19자리까지 표현할 수 있다.")
    void priceRange() {
        final Product product = new Product();
        product.setName("쫀득쫀득 지렁이");

        product.setPrice(new BigDecimal("123451234512345123.12"));
        assertThatThrownBy(() -> productService.create(product))
                .as("가격의 자릿수가 19자리를 초과하면 저장할 수 없다.")
                .isInstanceOf(DataIntegrityViolationException.class);

        product.setPrice(new BigDecimal("12345123451234512.34"));
        assertThatCode(() -> productService.create(product))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("상품 이름은 255자까지 표현할 수 있다.")
    void nameSize() {
        final Product product = new Product();
        product.setPrice(new BigDecimal("4000"));

        product.setName("쫀".repeat(256));
        assertThatThrownBy(() -> productService.create(product))
                .as("이름의 길이가 255자를 초과하면 저장할 수 없다.")
                .isInstanceOf(DataIntegrityViolationException.class);

        product.setName("쫀".repeat(255));
        assertThatCode(() -> productService.create(product))
                .doesNotThrowAnyException();
    }
}
