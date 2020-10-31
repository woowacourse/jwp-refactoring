package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;

@SpringBootTest(classes = {
        ProductRepository.class,
        ProductService.class
})
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("create: 상품 생성")
    @Test
    void create() {
        final Product product = createProduct("매콤치킨", BigDecimal.valueOf(16000));
        final Product actual = productService.create(product);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("매콤치킨");
        assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(16000));
    }

    @DisplayName("create: 상품의 가격이 null일 때 예외 처리")
    @Test
    void create_IfPriceIsNull_Exception() {
        final Product product = createProduct("매콤치킨", null);

        assertThatThrownBy(() ->productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 상품의 가격이 음수일 때 예외 처리")
    @Test
    void create_IfPriceIsNegative_Exception() {
        final Product product = createProduct("매콤치킨", BigDecimal.valueOf(-1));

        assertThatThrownBy(() ->productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 상품 전체 조회")
    @Test
    void list() {
        final Product product = createProduct("매콤치킨", BigDecimal.valueOf(16000));
        productService.create(product);

        final List<Product> products = productService.list();

        assertThat(products).isNotEmpty();
        assertThat(products).hasSize(1);
    }
}