package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product product = new Product();
        product.setName("후라이드치킨");
        product.setPrice(BigDecimal.valueOf(16000));

        given(productDao.save(any()))
                .willReturn(product);

        // when & then
        assertThat(productService.create(product)).isEqualTo(product);
        verify(productDao, times(1)).save(any());
    }

    @DisplayName("상품의 가격이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenPriceNull() {
        // given
        final Product product = new Product();
        product.setPrice(null);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0 이하이면 등록할 수 없다.")
    @Test
    void create_FailWhenPriceLessThanZero() {
        // given
        final Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Product product1 = new Product();
        final Product product2 = new Product();
        final List<Product> products = List.of(product1, product2);

        given(productDao.findAll())
                .willReturn(products);

        // when & then
        assertThat(productService.list()).isEqualTo(products);
        verify(productDao, times(1)).findAll();
    }
}
