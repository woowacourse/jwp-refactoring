package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.FieldNotValidException;
import kitchenpos.ui.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static kitchenpos.utils.RequestFactory.CREATE_PRODUCT_REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("상품 서비스 테스트")
class ProductServiceTest implements ServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("상품을 생성한다. - 실패, price가 Null인 경우")
    @Test
    void createFailedWhenPriceIsNull() {
        // given
        ProductRequest productRequest = CREATE_PRODUCT_REQUEST("강정치킨", null);

        // when
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(FieldNotValidException.class);
        then(productRepository).should(never())
                .save(any(Product.class));
    }

    @DisplayName("상품을 생성한다. - 실패, price가 0보다 작은 경우")
    @Test
    void createFailedWhenPriceLessThanZero() {
        // given
        ProductRequest productRequest = CREATE_PRODUCT_REQUEST("강정치킨", BigDecimal.valueOf(-1));

        // when
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(FieldNotValidException.class);
        then(productRepository).should(never())
                .save(any(Product.class));
    }

}
