package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductServiceTest extends ServiceBaseTest {

    @Autowired
    protected ProductService productService;

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void create() {
        //given
        final ProductRequest request = new ProductRequest("오션 메뉴 그룹", new BigDecimal(5000));

        //when
        final ProductResponse response = productService.create(request);

        //then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getPrice().intValue()).isEqualTo(request.getPrice().intValue())
        );
    }

    @Test
    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    void priceOverZero() {
        //given
        final ProductRequest request = new ProductRequest("오션 메뉴 그룹", new BigDecimal(-1));

        //when&then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("잘못된 가격입니다.");
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다.")
    void list() {
        //given
        final ProductRequest request = new ProductRequest("오션 메뉴 그룹", new BigDecimal(5000));
        final ProductResponse response = productService.create(request);

        //when
        final List<ProductResponse> responses = productService.list();

        //then
        assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses.get(0).getName()).isEqualTo(response.getName()),
                () -> assertThat(responses.get(0).getPrice().intValue()).isEqualTo(response.getPrice().intValue())
        );
    }
}
