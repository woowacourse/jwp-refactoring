package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
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

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        // given
        final Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setName("상품");

        given(productDao.save(any()))
            .willReturn(new Product() {{
                setId(1L);
                setPrice(BigDecimal.TEN);
                setName("상품");
            }});

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isEqualTo(1L);
        assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.TEN);
        assertThat(savedProduct.getName()).isEqualTo("상품");
    }

    @DisplayName("상품의 가격이 null 이면 예외가 발생한다.")
    @Test
    void create_failNullPrice() {
        // given
        final Product product = new Product();
        product.setPrice(null);

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 음수이면 예외가 발생한다.")
    @Test
    void create_failNegativePrice() {
        // given
        final Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1L));

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
