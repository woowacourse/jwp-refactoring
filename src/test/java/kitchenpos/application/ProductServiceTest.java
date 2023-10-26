package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.application.support.domain.ProductTestSupport;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService target;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        //given
        final ProductCreateRequest request = ProductTestSupport.builder().buildToProductCreateRequest();

        //when

        //then
        Assertions.assertDoesNotThrow(() -> target.create(request));
    }

    @DisplayName("상품의 가격이 음수이면 예외 처리한다.")
    @Test
    void create_fail_price_minus() {
        //given
        final BigDecimal price = new BigDecimal("-1");
        final ProductCreateRequest request = ProductTestSupport.builder().price(price).buildToProductCreateRequest();

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 상품을 조회한다.")
    @Test
    void list() {
        //given
        final BigDecimal price1 = new BigDecimal("1000");
        final Product product1 = ProductTestSupport.builder().price(price1).build();
        final Product product2 = ProductTestSupport.builder().build();

        given(productRepository.findAll()).willReturn(List.of(product1, product2));

        //when
        final List<ProductResponse> result = target.list();

        //then
        assertThat(result).extracting(ProductResponse::getName)
                .contains(product1.getName(), product2.getName());
    }
}
