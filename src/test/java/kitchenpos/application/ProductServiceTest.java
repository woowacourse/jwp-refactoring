package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.repository.ProductRepository;
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

    @DisplayName("product 생성")
    @Test
    void create() {
        ProductRequest productRequest = new ProductRequest("후라이드", 16000.00);

        productService.create(productRequest);

        verify(productRepository).save(any(Product.class));
    }

    @DisplayName("product 불러오기")
    @Test
    void list() {
        productService.list();

        verify(productRepository).findAll();
    }

}
