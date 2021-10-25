package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.exception.KitchenposException.ILLEGAL_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest extends ServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        when(productDao.save(any(Product.class)))
                .thenReturn(product);

        Product actual = productService.create(product);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(product);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -1000})
    @DisplayName("상품의 가격이 0보다 작으면 예외가 발생한다.")
    void createExceptionMinusPrice(Integer price) {
        product.setPrice(BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_PRICE);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("상품의 가격이 없으면 예외가 발생한다.")
    void createExceptionEmptyPrice(BigDecimal price) {
        product.setPrice(price);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_PRICE);
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void list() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("단무지");
        product2.setPrice(BigDecimal.valueOf(100));

        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);

        when(productDao.findAll())
                .thenReturn(products);

        assertThat(productService.list()).usingRecursiveComparison()
                .isEqualTo(products);
    }
}
