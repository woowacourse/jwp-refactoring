package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dtos.ProductRequest;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create() {
        final ProductRequest request = new ProductRequest("컵누들", 1500L);

        final Product savedProduct = Product.builder()
                .name("컵누들")
                .price(BigDecimal.valueOf(1500))
                .id(1L)
                .build();

        when(productRepository.save(any())).thenReturn(savedProduct);

        final Product actual = productService.create(request);
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(1500)),
                () -> assertThat(actual.getName()).isEqualTo("컵누들")
        );
    }

    @DisplayName("상품의 가격은 0 원 이상이어야 한다")
    @Test
    void createExceptionPriceUnderZero() {
        final ProductRequest request = new ProductRequest("컵누들", -1L);

        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 비어있어서는 안 된다")
    @Test
    void createExceptionPriceNull() {
        final ProductRequest request = new ProductRequest("컵누들", null);

        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void list() {
        final Product product1 = new Product();
        final Product product2 = new Product();
        final List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        final List<Product> actual = productService.list();
        assertThat(actual).containsExactly(product1, product2);
    }
}
