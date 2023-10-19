package kitchenpos.application;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
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
        productService.create(product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("상품 목록을 확인할 수 있다.")
    void list_success() {
        productService.list();

        verify(productRepository, times(1)).findAll();
    }
}
