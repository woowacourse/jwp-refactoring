package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() {
        Product product = createProduct();
        when(productDao.save(any())).thenReturn(createProduct(1L));

        Product actual = productService.create(product);

        verify(productDao).save(any(Product.class));
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isNotNull(),
                () -> assertThat(actual.getPrice()).isNotNull()
        );
    }

    @DisplayName("상품의 가격이 0원 미만일 경우 생성할 수 없다.")
    @Test
    void createExceptionIfPriceZero() {
        Product product = createProduct();
        product.setPrice(BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        Product product1 = createProduct(1L);
        Product product2 = createProduct(2L);
        when(productDao.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> actual = productService.list();

        verify(productDao).findAll();
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(product1, product2)
        );
    }
}