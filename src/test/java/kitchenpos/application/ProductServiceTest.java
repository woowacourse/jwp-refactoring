package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create() {
        final Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1500));
        product.setName("컵누들");
        final Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setPrice(BigDecimal.valueOf(1500));
        savedProduct.setName("컵누들");

        when(productDao.save(product)).thenReturn(savedProduct);

        final Product actual = productService.create(product);
        assertThat(actual.getId()).isEqualTo(1L);
    }

    @DisplayName("상품의 가격은 0 원 이상이어야 한다")
    @Test
    void createExceptionPriceUnderZero() {
        final Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1));
        product.setName("컵누들");

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 비어있어서는 안 된다")
    @Test
    void createExceptionPriceNull() {
        final Product product = new Product();
        product.setPrice(null);
        product.setName("컵누들");

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void list() {
        final Product product1 = new Product();
        final Product product2 = new Product();
        final List<Product> products = Arrays.asList(product1, product2);

        when(productDao.findAll()).thenReturn(products);

        final List<Product> actual = productService.list();
        assertThat(actual).containsExactly(product1, product2);
    }
}
