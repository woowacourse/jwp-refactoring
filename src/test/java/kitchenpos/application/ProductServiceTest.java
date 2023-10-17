package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.application.exception.ProductServiceException.NoPriceException;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
    private ProductDao productDao;

    private Product product = new Product();

    @BeforeEach
    void init() {
        product.setPrice(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void create_success() {
        productService.create(product);

        verify(productDao, times(1)).save(product);
    }

    @Test
    @DisplayName("상품 목록을 확인할 수 있다.")
    void list_success() {
        productService.list();

        verify(productDao, times(1)).findAll();
    }
}
