package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    class create는 {

        private final Long id = 1L;
        private final String name = "pasta";
        private final BigDecimal price = BigDecimal.valueOf(13000);

        @Test
        void 상품을_생성할_수_있다() {
            // given
            ProductCreateRequest request = 상품_생성_dto를_만든다(id, name, price);
            when(productRepository.save(any(Product.class))).thenReturn(request.toProduct());

            // when
            ProductResponse response = productService.create(request);

            // then
            assertThat(response.getName()).isEqualTo("pasta");
        }

        @Test
        void price가_null이면_예외를_반환한다() {
            BigDecimal nullPrice = null;
            ProductCreateRequest request = 상품_생성_dto를_만든다(id, name, nullPrice);
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void price가_0보다_작으면_예외를_반환한다() {
            // given
            BigDecimal negativePrice = BigDecimal.valueOf(-1000);
            ProductCreateRequest request = 상품_생성_dto를_만든다(id, name, negativePrice);
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private ProductCreateRequest 상품_생성_dto를_만든다(final Long id, final String name, final BigDecimal price) {
            return new ProductCreateRequest(name, price);
        }
    }

    @Nested
    class list는 {

        @Test
        void 상품_목록을_조회할_수_있다() {
            Product product = new Product(1L, "pasta", BigDecimal.valueOf(13000));
            when(productRepository.findAll()).thenReturn(Arrays.asList(product));
            List<ProductResponse> responses = productService.list();

            assertThat(responses).hasSize(1)
                    .usingRecursiveComparison()
                    .ignoringFields("price")
                    .isEqualTo(Arrays.asList(product));
        }
    }
}
