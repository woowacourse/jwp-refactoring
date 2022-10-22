package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
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
    void 상품을_생성할_수_있다() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(13000));
        product.setName("pasta");
        when(productDao.save(any(Product.class))).thenReturn(product);

        // when
        Product savedProduct = productService.create(product);

        // then
        Assertions.assertAll(
                () -> assertThat(savedProduct.getId()).isEqualTo(1L),
                () -> assertThat(savedProduct.getName()).isEqualTo("pasta")
        );
    }

    @Test
    void price가_null이면_예외를_반환한다() {
        // given
        Product product = new Product();

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_0보다_작으면_예외를_반환한다() {
        // given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(13000));
        product.setName("pasta");
        when(productDao.findAll()).thenReturn(Arrays.asList(product));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("price")
                .isEqualTo(
                        Arrays.asList(product)
                );
    }
}
