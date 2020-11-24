package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

class ProductServiceTest extends ServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        String name = "고추마요 치킨";
        BigDecimal price = BigDecimal.valueOf(18_000);
        Product productRequest = createProduct(null, name, price);

        Product saved = productService.create(productRequest);
        assertAll(
            () -> assertThat(saved.getId()).isNotNull(),
            () -> assertThat(saved.getName()).isEqualTo(name),
            () -> assertThat(saved.getPrice().longValue()).isEqualTo(price.longValue())
        );
    }

    @DisplayName("상품 등록 시, 상품 가격이 음수 혹은 null이면 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = "-1000")
    @NullSource
    void create_WithNegativePrice_ThrownException(BigDecimal price) {
        Product productRequest = createProduct(null, "고추마요 치킨", price);

        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        BigDecimal price = BigDecimal.valueOf(18_000);
        Product productRequest = createProduct(null, "고추마요 치킨", price);
        productDao.save(productRequest);

        List<Product> actual = productService.list();

        assertThat(actual).hasSize(1);
    }
}