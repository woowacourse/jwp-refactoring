package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Test
    void 상품_가격은_NULL_일_수_없다() {
        Product product = new Product();
        product.setPrice(null);

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격은_음수일_수_없다() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성할_수_있다() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        productService.create(product);

        verify(productDao).save(product);
    }

    @Test
    void 전체_상품_조회할_수_있다() {
        productService.list();

        verify(productDao).findAll();
    }
}
