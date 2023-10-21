package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductRepository;
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
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product product = new Product(100L, "후라이드 치킨", BigDecimal.valueOf(16000));

        given(productRepository.save(any()))
                .willReturn(product);

        // when & then
        assertThat(productService.create(product)).isEqualTo(product);
        then(productRepository).should(times(1)).save(any());
    }

    @DisplayName("상품의 가격이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenPriceNull() {
        // given
        final Product product = new Product(100L, "후라이드 치킨", null);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0 미만이면 등록할 수 없다.")
    @Test
    void create_FailWhenPriceLessThanZero() {
        // given
        final Product product = new Product(100L, "후라이드 치킨", BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Product product1 = new Product(100L, "후라이드 치킨", BigDecimal.valueOf(16000));
        final Product product2 = new Product(101L, "양념 치킨", BigDecimal.valueOf(17000));
        final List<Product> products = List.of(product1, product2);

        given(productRepository.findAll())
                .willReturn(products);

        // when & then
        assertThat(productService.list()).isEqualTo(products);
        then(productRepository).should(times(1)).findAll();
    }
}
