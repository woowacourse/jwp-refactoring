package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Name;
import kitchenpos.domain.product.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJdbcTest
class ProductServiceTest {

    private ProductService productService;

    @Autowired
    ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("이름과 가격을 제공하면 새로운 상품을 제공할 수 있다.")
    void givenNameAndPrice() {
        final ProductResponse savedProduct = this.productService.create(new Name("쫀득쫀득 지렁이"), new Price(new BigDecimal("4000")));
        assertThat(savedProduct).isNotNull();
    }

    @Test
    @DisplayName("가격 정보는 비어있거나 음수의 값이 아니어야 한다.")
        // FIXME : 도메인 단위 테스트
    void whenInvalidPrice() {
        assertThatThrownBy(() -> productService.create(new Name("쫀득쫀득 지렁이"), new Price(null)))
                .as("가격이 비어있는 경우 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> productService.create(new Name("쫀득쫀득 지렁이"), new Price(new BigDecimal("-1"))))
                .as("가격이 음수인 경우에도 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 소수점 2자리를 포함해 총 19자리까지 표현할 수 있다.")
        // FIXME : 도메인 단위 테스트
    void priceRange() {
        assertThatThrownBy(() -> productService.create(new Name("쫀득쫀득 지렁이"), new Price(new BigDecimal("123451234512345123.12"))))
                .as("가격의 자릿수가 19자리를 초과하면 저장할 수 없다.")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);

        assertThatCode(() -> productService.create(new Name("쫀득쫀득 지렁이"), new Price(new BigDecimal("12345123451234512.34"))))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("상품 이름은 255자까지 표현할 수 있다.")
        // FIXME : 도메인 단위 테스트
    void nameSize() {
        assertThatThrownBy(() -> productService.create(new Name("쫀".repeat(256)), new Price(new BigDecimal("4000"))))
                .as("이름의 길이가 255자를 초과하면 저장할 수 없다.")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);

        assertThatCode(() -> productService.create(new Name("쫀".repeat(255)), new Price(new BigDecimal("4000"))))
                .doesNotThrowAnyException();
    }
}
