package kitchenpos.integration.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductRequestDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceIntegrationTest extends IntegrationTest {

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        ProductRequestDto requestDto = new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000));

        // when
        ProductResponseDto responseDto = productService.create(requestDto);

        // then
        assertThat(responseDto.getId()).isNotNull();
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다. - null인 경우")
    @Test
    void create_InvalidPriceWithNull_Fail() {
        // given
        ProductRequestDto requestDto = new ProductRequestDto("얌 프라이", null);

        // when
        // then
        assertThatThrownBy(() -> productService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다. - 0 미만인 경우")
    @Test
    void create_InvalidPriceWithNegative_Fail() {
        // given
        ProductRequestDto requestDto = new ProductRequestDto("얌 프라이", BigDecimal.valueOf(-1000));

        // when
        // then
        assertThatThrownBy(() -> productService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void list_Valid_Success() {
        // given
        ProductRequestDto requestDto1 = new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000));
        ProductRequestDto requestDto2 = new ProductRequestDto("포테이토 피자", BigDecimal.valueOf(16000));

        productService.create(requestDto1);
        productService.create(requestDto2);

        // when
        List<ProductResponseDto> responsesDto = productService.list();

        // then
        assertThat(responsesDto).hasSize(2);
    }
}
