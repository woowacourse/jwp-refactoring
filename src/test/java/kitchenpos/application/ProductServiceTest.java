package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductResponse;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_등록할_수_있다() {
        // given
        final ProductCreateRequest request = 강정치킨_생성_요청();

        // when
        ProductResponse actual = productService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(request.getName()),
                () -> assertThat(actual.getPrice())
                        .isCloseTo(request.getPrice(), Percentage.withPercentage(0))
        );
    }

    @Test
    void 상품_목록들을_조회한다() {
        // given
        productService.create(강정치킨_생성_요청());

        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    public ProductCreateRequest 강정치킨_생성_요청() {
        return new ProductCreateRequest("강정치킨", 17000);
    }
}
