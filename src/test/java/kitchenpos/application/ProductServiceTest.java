package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.inmemorydao.InMemoryProductDao;

@DisplayName("ProductService 테스트")
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

    @DisplayName("메뉴의 가격이 올바르지 않은 경우 예외가 발생한다")
    @ParameterizedTest
    @MethodSource("generateInvalidPrice")
    void create_InvalidPrice_ExceptionThrown(final BigDecimal price) {
        // When
        final Product product = new Product();
        product.setPrice(price);

        // Then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    private static Stream<Arguments> generateInvalidPrice() {
        return Stream.of(
                Arguments.arguments((BigDecimal)null),
                Arguments.arguments(BigDecimal.valueOf(-1000L))
        );
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
