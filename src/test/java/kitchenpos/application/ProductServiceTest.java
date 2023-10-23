package kitchenpos.application;

import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품을_등록한다() {
        // given

        // when, then
        productService.create(new ProductRequest("kong hana", BigDecimal.valueOf(99999999999999L)));

        then(productRepository).should(times(1)).save(any());
    }

    @Test
    void 상품의_가격이_음수면_예외발생() {
        // given
        ProductRequest productRequest = new ProductRequest("productRequest", BigDecimal.valueOf(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(productRepository).should(never()).save(any());
    }
}
