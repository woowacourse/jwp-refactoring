package kitchenpos.ui.v1.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.dto.MenuUpdateRequest;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.product.dto.ProductCreateRequest;
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

    @Nested
    class update {

        @Test
        void 가격이_음수이면_400() throws Exception {
            // given
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 5000));
            메뉴_생성(new MenuCreateRequest(25000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(5, 1L)
            )));

            // when & then
            메뉴_수정(1L, new MenuUpdateRequest("소주세트", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("메뉴의 가격은 0보다 작을 수 없습니다."));
        }

        @Test
        void 메뉴_상품의_가격_총합보다_메뉴의_가격이_크면_400() throws Exception {
            // given
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 5000));
            메뉴_생성(new MenuCreateRequest(25000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(5, 1L)
            )));

            // when & then
            메뉴_수정(1L, new MenuUpdateRequest("소주세트", 25001L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("메뉴의 가격은 메뉴 상품의 총합 가격보다 작아야 합니다."));
        }

        @Test
        void 성공하면_200() throws Exception {
            // given
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 5000));
            메뉴_생성(new MenuCreateRequest(25000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(5, 1L)
            )));

            // when & then
            메뉴_수정(1L, new MenuUpdateRequest("소주세트", 20000L))
                .andExpect(status().isOk());
        }

        @Test
        void 수정하면_상태가_변경() throws Exception {
            // given
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 5000));
            메뉴_생성(new MenuCreateRequest(25000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(5, 1L)
            )));
            메뉴_수정(1L, new MenuUpdateRequest("소주세트", 20000L));

            // when
            모든_메뉴_조회()
                .andExpect(jsonPath("$[0].name").value("소주세트"))
                .andExpect(jsonPath("$[0].price").value("20000.0"));
        }
    }
}
