package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        ProductRequest productRequest = new ProductRequest("떡볶이", 3500);

        //when
        ProductResponse actual = productService.create(productRequest);
        //then
        assertThat(actual.getName()).isEqualTo("떡볶이");
        assertThat(actual.getPrice()).isEqualTo(new BigDecimal(3500));
    }

    @DisplayName("상품을 등록 실패 - 유효하지 않은 가격일 경우")
    @Test
    void createFailInvalidPrice() {
        //given
        ProductRequest productRequest = new ProductRequest("떡볶이", -3500);

        //when
        //then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(InvalidPriceException.class);
    }
}
