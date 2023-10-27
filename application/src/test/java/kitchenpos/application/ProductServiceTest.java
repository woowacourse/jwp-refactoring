package kitchenpos.application;

import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductServiceTest extends ServiceTest {
    private ProductCreateRequest productCreateRequest;

    @BeforeEach
    void setUp() {
        productCreateRequest = makeProductCreateRequestByPrice(BigDecimal.valueOf(17000));
    }

    @Test
    void 상품을_생성한다() {
        ProductResponse response = productService.create(productCreateRequest);
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(productCreateRequest.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(productCreateRequest.getPrice())
        );
    }

    @Test
    void 가격이_0미만이면_예외발생() {
        assertThatThrownBy(
                () -> productService.create(makeProductCreateRequestByPrice(BigDecimal.valueOf(-1)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_상품_조회() {
        List<ProductResponse> responses = productService.list();
        assertThat(responses.size()).isEqualTo(6);
    }

    private ProductCreateRequest makeProductCreateRequestByPrice(BigDecimal price) {
        return new ProductCreateRequest("강정치킨", price);
    }
}
