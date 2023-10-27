package kitchenpos.product.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void init() {
        product = Product.of("kong", BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void create_success() {
        when(productRepository.save(any())).thenReturn(product);

        ProductRequest productRequest = new ProductRequest("wuga", BigDecimal.valueOf(1000));
        Assertions.assertThatCode(() -> productService.create(productRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("상품 목록을 확인할 수 있다.")
    void list_success() {
        productService.list();

        verify(productRepository, times(1)).findAllByDeletedFalse();
    }
}
