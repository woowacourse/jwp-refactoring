package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("상품 서비스 테스트")
@MockitoSettings
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @DisplayName("상품을 생성한다. - 실패, price가 Null인 경우")
    @Test
    void createFailedWhenPriceIsNull() {
        // given
        Product product = new Product("강정치킨", null);

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        then(productDao).should(never())
                .save(any(Product.class));
    }

    @DisplayName("상품을 생성한다. - 실패, price가 0보다 작은 경우")
    @Test
    void createFailedWhenPriceLessThanZero() {
        // given
        Product product = new Product("강정치킨", BigDecimal.valueOf(-1));

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        then(productDao).should(never())
                .save(any(Product.class));
    }

}
