package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("새 상품을 저장한다.")
    @Test
    void create_success() {
        // given
        final Product product = new Product();
        product.setName("name");
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(1000L));

        given(productDao.save(any(Product.class)))
            .willReturn(product);

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isEqualTo(product);
    }

    @DisplayName("새 상품의 가격이 0보다 작다면 예외가 발생한다.")
    @Test
    void create_wrongPrice_fail() {
        // given
        final Product product = new Product();
        product.setName("name");
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(-1L));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void list() {
        // given
        final Product product1 = new Product();
        product1.setName("name");
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(1000L));

        final Product product2 = new Product();
        product2.setName("name2");
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(1000L));

        given(productDao.findAll())
            .willReturn(List.of(product1, product2));

        // when
        final List<Product> foundProducts = productService.list();

        // then
        assertThat(foundProducts).containsExactly(product1, product2);
    }
}
