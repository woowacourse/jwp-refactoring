package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.inmemorydao.InMemoryProductDao;

class ProductServiceTest {
    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        this.productDao = new InMemoryProductDao();
        this.productService = new ProductService(productDao);
    }

    @DisplayName("상품을 등록한다")
    @Test
    void create() {
        // Given
        final Product product = new Product();
        product.setName("파닭치킨");
        product.setPrice(BigDecimal.valueOf(18000L));

        // When
        final Product savedProduct = productService.create(product);

        // Then
        assertThat(savedProduct)
                .extracting(Product::getId)
                .isNotNull()
        ;
    }

    @DisplayName("금액이 null일 경우 예외가 발생한다")
    @Test
    void create_PriceIsNull() {
        // Given
        final Product product = new Product();

        // Then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("금액이 음수일 경우 예외가 발생한다")
    @Test
    void create_PriceIsNegative() {
        // Given
        final Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1000L));

        // Then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("상품의 목록을 조회한다")
    @Test
    void list() {
        // Given
        final Product product = new Product();
        product.setName("파닭치킨");
        product.setPrice(BigDecimal.valueOf(18000L));
        productService.create(product);

        // When
        final List<Product> list = productService.list();

        // Then
        assertThat(list).isNotEmpty();
    }
}
