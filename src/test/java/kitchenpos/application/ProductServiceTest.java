package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.persistence.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    class 상품_생성 {
        @Test
        void 상품을_생성한다() {
            // given
            final Product savedProduct = new Product(1L, "상품", BigDecimal.valueOf(1000));
            when(productRepository.save(any(Product.class)))
                    .thenReturn(savedProduct);

            // when
            final Product product = new Product("상품", BigDecimal.valueOf(1000));
            final Product result = productService.create(product);

            // then
            assertThat(result).isEqualTo(savedProduct);
        }

        @Test
        void 상품을_생성할_때_가격이_없으면_실패한다() {
            // given
            final Product product = new Product("상품", null);

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품을_생성할_때_가격이_0보다_작으면_실패한다() {
            // given
            final Product product = new Product("상품", BigDecimal.valueOf(-1000));

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_상품_목록을_반환한다() {
        // given
        when(productRepository.findAll())
                .thenReturn(Collections.emptyList());

        // when
        final List<Product> result = productService.list();

        // then
        assertThat(result).isEmpty();
    }
}