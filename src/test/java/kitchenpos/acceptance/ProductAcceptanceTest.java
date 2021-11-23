package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.ui.dto.request.ProductRequest;
import kitchenpos.ui.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품 생성 테스트 - 성공")
    public void createTest() throws Exception {
        // given
        ProductRequest 치킨_생성_요청 = productFixture.상품_생성_요청("치킨", BigDecimal.TEN);

        // when
        ProductResponse actual = 상품_등록(치킨_생성_요청);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(productFixture.상품_조회(actual.getId()));
    }

    @Test
    @DisplayName("상품 리스트 조회 테스트 - 성공")
    public void listTest() throws Exception {
        // given
        ProductRequest 치킨_생성_요청 = productFixture.상품_생성_요청("치킨", BigDecimal.TEN);
        ProductRequest 피자_생성_요청 = productFixture.상품_생성_요청("피자", BigDecimal.TEN);
        List<ProductResponse> expected = productFixture.상품_리스트_생성(상품_등록(치킨_생성_요청), 상품_등록(피자_생성_요청));

        // when
        List<ProductResponse> actual = 상품_리스트_조회();

        // then
        assertThat(actual).hasSize(expected.size());
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
