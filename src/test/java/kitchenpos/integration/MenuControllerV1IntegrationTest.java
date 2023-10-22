package kitchenpos.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import kitchenpos.ui.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuControllerV1IntegrationTest extends V1IntegrationTest {

    @Nested
    class create {

        @Test
        void 메뉴의_가격이_상품의_총합_가격보다_크면_400() throws Exception {
            // given
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 5000));

            var request = new MenuCreateRequest(25001, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(5, 1L)
            ));

            // when & then
            메뉴_생성(request)
                .andExpect(jsonPath("$.message").value("메뉴의 가격은 메뉴 상품의 총합 가격보다 작아야 합니다."));
        }

        @Test
        void 가격이_음수이면_400() throws Exception {
            // given
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 5000));

            var request = new MenuCreateRequest(-1, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(5, 1L)
            ));

            // when & then
            메뉴_생성(request)
                .andExpect(jsonPath("$.message").value("메뉴의 가격은 0보다 작을 수 없습니다."));
        }

        @Test
        void 성공하면_201() throws Exception {
            // given
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 5000));

            var request = new MenuCreateRequest(25000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(5, 1L)
            ));

            // when & then
            메뉴_생성(request)
                .andExpect(status().isCreated());
        }
    }
}
