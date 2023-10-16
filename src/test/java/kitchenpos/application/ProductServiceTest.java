package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품을_등록한다() {
        // given
        Product product = new Product();
        product.setName("kong hana");
        product.setPrice(BigDecimal.valueOf(99999999999999L));

        // when, then
        productService.create(product);

        then(productDao).should(times(1)).save(any());
    }

    @Test
    void 상품의_가격이_음수면_예외발생() {
        // given
        Product product = new Product();
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        then(productDao).should(never()).save(any());
    }
}
