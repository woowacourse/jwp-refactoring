package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.ProductFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ProductServiceTest extends ServiceTest{

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductFixtures.createProduct();
    }

    @Test
    void 상품을_생성한다() {
        given(productDao.save((any()))).willReturn(product);

        assertDoesNotThrow(() -> productService.create(product));
        verify(productDao, times(1)).save(any());
    }

    @Test
    void 생성_시_가격이_음수이면_예외를_반환한다() {
        Product invalidPriceProduct = ProductFixtures.createProduct(-1000);

        assertThrows(IllegalArgumentException.class, () -> productService.create(invalidPriceProduct));
    }

    @Test
    void 상품_리스트를_반환한다() {
        given(productDao.findAll()).willReturn(Collections.singletonList(product));

        assertDoesNotThrow(() -> productService.list());
    }
}