package kitchenpos.ui.v1.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductUpdateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductControllerV1IntegrationTest extends V1IntegrationTest {

    @Nested
    class create {

        @Test
        void 가격이_음수이면_400() throws Exception {
            // given
            var request = new ProductCreateRequest("맥주", -1);

            // when & then
            상품_생성(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("상품의 가격은 0보다 작을 수 없습니다."));
        }

        @Test
        void 성공하면_201() throws Exception {
            // given
            var request = new ProductCreateRequest("맥주", 0);

            // when & then
            상품_생성(request)
                .andExpect(status().isCreated());
        }
    }

    @Nested
    class update {

        @Test
        void 가격이_음수이면_400() throws Exception {
            // given
            상품_생성(new ProductCreateRequest("맥주", 4885));

            var request = new ProductUpdateRequest("소주", -1L);

            // when & then
            상품_수정(1L, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("상품의 가격은 0보다 작을 수 없습니다."));
        }

        @Test
        void 성공하면_200() throws Exception {
            // given
            상품_생성(new ProductCreateRequest("맥주", 4885));

            var request = new ProductUpdateRequest("소주", 2000L);

            // when & then
            상품_수정(1L, request)
                .andExpect(status().isOk());
        }

        @Test
        void 수정하면_상태가_변경() throws Exception {
            // given
            상품_생성(new ProductCreateRequest("맥주", 4885));
            상품_수정(1L, new ProductUpdateRequest("소주", 2000L));

            // when & then
            모든_상품_조회()
                .andExpect(jsonPath("$[0].name").value("소주"))
                .andExpect(jsonPath("$[0].price").value("2000.0"));
        }
    }
}
