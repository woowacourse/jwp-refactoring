package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductDao productDao;


    @Nested
    class CreateTest {
        @Test
        @DisplayName("가격이 null이면 예외가 발생한다.")
        void priceIsNull() {
            final ProductRequest request = new ProductRequest("product", null);
            assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수이면 예외가 발생한다.")
        void priceIsNegative() {
            final ProductRequest request = new ProductRequest("product", BigDecimal.valueOf(-1));
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품을 생성한다.")
        void createProduct() {
            // given
            final ProductRequest request = new ProductRequest("product", BigDecimal.valueOf(10_000));
            final Product product = new Product(1L, "product", BigDecimal.valueOf(10_000));
            given(productDao.save(any())).willReturn(product);

            // when
            final Product result = productService.create(request);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(product);
        }
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void list() {
        // given
        final List<Product> products = List.of(
                new Product(1L, "product1", BigDecimal.valueOf(10_000)),
                new Product(2L, "product2", BigDecimal.valueOf(20_000)),
                new Product(3L, "product3", BigDecimal.valueOf(30_000))
        );
        given(productDao.findAll()).willReturn(products);

        // when
        final List<Product> result = productService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(products);
    }
}
