package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("생성")
    void create() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));

        given(productDao.save(any(Product.class))).willReturn(product);

        Product saved = productService.create(product);

        assertThat(saved).isNotNull();
        assertThat(saved.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품의 가격이 null이거나 0미만인 경우")
    void priceBelowZero() {
        Product product = new Product();
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);

        product.setPrice(BigDecimal.valueOf(-1000));
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
