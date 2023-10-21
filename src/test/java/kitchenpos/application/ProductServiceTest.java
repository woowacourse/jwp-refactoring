package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void 상품_생성할_수_있다() {
        ProductCreateRequest request = new ProductCreateRequest("로제떡볶이", 1000);

        productService.create(request);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void 전체_상품_조회할_수_있다() {
        productService.list();

        verify(productRepository).findAll();
    }
}
