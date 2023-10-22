package kitchenpos.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.ui.dto.ProductCreateRequest;
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
}
