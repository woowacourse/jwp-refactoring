package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.ProductFixtures;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ProductServiceTest extends ServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductFixtures.createProduct();
    }

    @Test
    void 상품을_생성한다() {
        given(productRepository.save((any()))).willReturn(product);

        assertDoesNotThrow(() -> productService.create(ProductFixtures.createProductRequest()));
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void 생성_시_가격이_음수이면_예외를_반환한다() {
        ProductRequest invalidRequest = ProductFixtures.createProductRequest(-1);

        assertThrows(IllegalArgumentException.class, () -> productService.create(invalidRequest));
    }

    @Test
    void 상품_리스트를_반환한다() {
        given(productRepository.findAll()).willReturn(Collections.singletonList(product));

        assertDoesNotThrow(() -> productService.list());
    }
}