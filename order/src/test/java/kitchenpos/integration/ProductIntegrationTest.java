package kitchenpos.integration;

import static kitchenpos.integration.api.texture.ProductTexture.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.integration.api.ProductApi;
import kitchenpos.product.ui.request.ProductCreateRequest;
import kitchenpos.product.response.ProductResponse;
import kitchenpos.testtool.IntegrationTest;
import kitchenpos.testtool.MockMvcResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ProductIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductApi productApi;

    @Test
    @DisplayName("상품 등록 성공")
    public void 상품_등록() {
        // when
        final MockMvcResponse<ProductResponse> result = productApi.상품_등록(강정치킨);

        // then
        assertThat(result.getContent().getName()).isEqualTo(강정치킨.getProduct().getName());
        assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("상품 등록 실패 - 가격 미등록")
    public void 상품_등록_실패() {
        // when
        final MockMvcResponse<ProductResponse> result = productApi.상품_등록(
            ProductCreateRequest.create("강정치킨", null));

        // then
        assertThat(result.getErrorMessage()).isEqualTo("등록하는 가격이 비어있습니다.");
        assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 기존_데이터_조회() {
        final List<ProductResponse> response = productApi.상품_검색().getContent();

        assertThat(response).extracting(ProductResponse::getName)
            .containsExactlyInAnyOrder("후라이드","양념치킨","반반치킨","통구이","간장치킨","순살치킨");
    }
}
