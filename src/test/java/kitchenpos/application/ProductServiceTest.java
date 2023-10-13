package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Test
    void testCreateSuccess() {
        //given
        final Product product = new Product("test", BigDecimal.valueOf(1000));
        final Product expected = new Product(1L, "test", BigDecimal.valueOf(1000));
        when(productDao.save(product))
                .thenReturn(expected);

        //when
        final Product result = productService.create(product);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateWhenPriceNull() {
        //given
        final Product product = new Product("test", null);

        //when
        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCreateWhenPriceLowerThanZero() {
        //given
        final Product product = new Product("test", BigDecimal.valueOf(-1));

        //when
        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testListSuccess() {
        //given
        final Product product1 = new Product(1L, "test1", BigDecimal.valueOf(1000));
        final Product product2 = new Product(2L, "test2", BigDecimal.valueOf(1000));
        final Product product3 = new Product(3L, "test3", BigDecimal.valueOf(1000));

        when(productDao.findAll()).thenReturn(List.of(product1, product2, product3));

        //when
        final List<Product> results = productService.list();

        //then
        assertThat(results).isEqualTo(List.of(product1, product2, product3));
    }
}
