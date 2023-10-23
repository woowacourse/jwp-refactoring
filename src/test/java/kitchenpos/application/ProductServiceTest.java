package kitchenpos.application;

import static kitchenpos.application.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.product.ProductRequest;
import kitchenpos.application.dto.product.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("새 상품을 저장한다.")
    @Test
    void create_success() {
        // given
        final Product product = createProduct(1L, "product", 1000L);
        final ProductRequest productRequest = new ProductRequest("product", BigDecimal.valueOf(1000L));

        given(productDao.save(any(Product.class)))
            .willReturn(product);

        // when
        final ProductResponse savedProduct = productService.create(productRequest);

        // then
        assertThat(savedProduct).usingRecursiveComparison()
            .isEqualTo(ProductResponse.from(product));
    }

    @DisplayName("새 상품의 가격이 0보다 작다면 예외가 발생한다.")
    @Test
    void create_wrongPrice_fail() {
        // given
        final ProductRequest productRequest = new ProductRequest("product", BigDecimal.valueOf(-1L));

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void list() {
        // given
        final Product product1 = createProduct(1L, "product1", 1000L);
        final Product product2 = createProduct(2L, "product2", 1000L);

        given(productDao.findAll())
            .willReturn(List.of(product1, product2));

        // when
        final List<ProductResponse> foundProducts = productService.list();

        // then
        assertThat(foundProducts).usingRecursiveComparison()
            .isEqualTo(List.of(ProductResponse.from(product1), ProductResponse.from(product2)));
    }
}
