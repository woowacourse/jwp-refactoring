package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.support.domain.ProductTestSupport;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;
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

        given(productDao.findAll()).willReturn(List.of(product1, product2));

        //when
        final List<Product> result = target.list();

        //then
        assertThat(result).extracting(Product::getName)
                .contains(product1.getName(), product2.getName());
    }
}
