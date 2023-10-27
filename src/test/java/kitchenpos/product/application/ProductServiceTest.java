package kitchenpos.product.application;

import kitchenpos.product.Product;
import kitchenpos.product.ProductName;
import kitchenpos.product.ProductPrice;
import kitchenpos.product.ProductRepository;
import kitchenpos.product.application.request.ProductRequest;
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
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @Nested
    class CreateTest {
        final ProductRequest request = mock(ProductRequest.class);

        @Test
        @DisplayName("가격을 입력하지 않으면 예외가 발생한다.")
        void priceIsNull() {
            // given
            given(request.getPrice()).willReturn(null);

            // when, then
            assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수이면 예외가 발생한다.")
        void priceIsNegative() {
            // given
            given(request.getPrice()).willReturn(BigDecimal.valueOf(-1));

            // when, then
            assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품을 생성한다.")
        void createProduct() {
            // given
            final Product product = new Product(1L, new ProductName("product"), new ProductPrice(BigDecimal.valueOf(10_000)));

            given(request.getPrice()).willReturn(product.getPrice());
            given(request.getName()).willReturn("productName");
            given(productRepository.save(any())).willReturn(product);

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
                new Product(new ProductName("product1"), new ProductPrice(BigDecimal.valueOf(10_000))),
                new Product(new ProductName("product2"), new ProductPrice(BigDecimal.valueOf(20_000))),
                new Product(new ProductName("product3"), new ProductPrice(BigDecimal.valueOf(30_000)))
        );
        given(productRepository.findAll()).willReturn(products);

        // when
        final List<Product> result = productService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(products);
    }
}
