package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성할_수_있다() {
        ProductCreateRequest request = new ProductCreateRequest("상품", BigDecimal.ZERO);

        ProductResponse response = productService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 상품_가격이_0원_미만이면_예외를_반환한다() {
        assertThatThrownBy(
                () -> productService.create(new ProductCreateRequest("상품", BigDecimal.valueOf(-1)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        Long productId = productService.create(new ProductCreateRequest("상품", BigDecimal.ZERO))
                .getId();

        List<ProductResponse> actual = productService.list();

        assertThat(actual).hasSize(1)
                .extracting("id")
                .containsOnly(productId);
    }
}
