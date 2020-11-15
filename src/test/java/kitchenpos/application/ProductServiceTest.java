package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Product;

class ProductServiceTest extends AbstractServiceTest {
    private ProductService productService;
    private JdbcTemplateProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);
        productService = new ProductService(productDao);
    }

    @DisplayName("상품의 가격이 없으면 예외를 반환한다.")
    @Test
    void priceIsNull() {
        Product product = ProductFixture.createWithOutId(null);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0원보다 작으면 예외를 반환한다.")
    @Test
    void priceUnder0() {
        Product product = ProductFixture.createWithOutId(BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품이 정상적으로 저장된다.")
    @Test
    void create() {
        Product product = ProductFixture.createWithOutId(BigDecimal.valueOf(1000L));
        Product savedProduct = productService.create(product);

        assertAll(
            () -> assertThat(savedProduct.getId()).isEqualTo(1L),
            () -> assertThat(product).isEqualToIgnoringGivenFields(product, "id")
        );
    }

    @DisplayName("저장된 모든 상품을 불러온다.")
    @Test
    void list() {
        Product product = ProductFixture.createWithOutId(BigDecimal.valueOf(1000L));
        List<Product> expected = Arrays.asList(
            productService.create(product),
            productService.create(product),
            productService.create(product)
        );
        List<Product> actual = productService.list();

        assertAll(
            () -> assertThat(actual).hasSize(3),
            () -> assertThat(actual).usingFieldByFieldElementComparator().containsAll(expected)
        );
    }
}