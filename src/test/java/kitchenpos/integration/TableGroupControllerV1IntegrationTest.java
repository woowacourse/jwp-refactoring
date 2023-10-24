package kitchenpos.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineCreateRequest;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableUpdateRequest;
import kitchenpos.ui.dto.OrderUpdateRequest;
import kitchenpos.ui.dto.ProductCreateRequest;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupControllerV1IntegrationTest extends V1IntegrationTest {


    @Nested
    class create {

        @Test
        void 주문_테이블이_비어있으면_400() throws Exception {
            // given
            var request = new TableGroupCreateRequest(Collections.emptyList());

            // when & then
            테이블_그룹_생성(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("주문 테이블 목록은 2개 이상이어야 합니다."));
        }

        @Test
        void 주문_테이블이_하나이면_400() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));

            var request = new TableGroupCreateRequest(List.of(1L));

            // when & then
            테이블_그룹_생성(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("주문 테이블 목록은 2개 이상이어야 합니다."));
        }

        @Test
        void 이미_테이블_그룹에_등록되어_있으면_400() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            테이블_그룹_생성(new TableGroupCreateRequest(List.of(1L, 2L)));

            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            var request = new TableGroupCreateRequest(List.of(1L, 3L));

            // when
            테이블_그룹_생성(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 테이블 그룹에 속해있는 주문 테이블 입니다."));
        }

        @Test
        void 비어있는_테이블이면_400() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 1000));
            메뉴_생성(new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(1, 1L)
            )));
            주문_생성(new OrderCreateRequest(1L, List.of(
                new OrderLineCreateRequest(1L, 3)
            )));
            주문_상태_변경(1L, new OrderUpdateRequest(OrderStatus.COMPLETION));
            주문_테이블_빈_테이블_변경(1L, new OrderTableUpdateRequest(true, 0));

            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            var request = new TableGroupCreateRequest(List.of(1L, 2L));

            // when & then
            테이블_그룹_생성(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비어있는 상태의 주문 테이블은 테이블 그룹에 등록할 수 없습니다."));
        }

        @Test
        void 성공하면_201() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));

            var request = new TableGroupCreateRequest(List.of(1L, 2L));

            // when & then
            테이블_그룹_생성(request)
                .andExpect(status().isCreated());
        }

        @Test
        void 성공하면_테이블_그룹이_설정() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            테이블_그룹_생성(new TableGroupCreateRequest(List.of(1L, 2L)));

            // when & then
            주문_테이블_모두_조회()
                .andExpect(jsonPath("$[0].tableGroupId").value("1"))
                .andExpect(jsonPath("$[1].tableGroupId").value("1"));
        }
    }

    @Nested
    class ungroup {

        @Test
        void 주문_테이블의_주문이_계산완료가_아니면_400() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 1000));
            메뉴_생성(new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(1, 1L)
            )));
            주문_생성(new OrderCreateRequest(1L, List.of(
                new OrderLineCreateRequest(1L, 3)
            )));
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            테이블_그룹_생성(new TableGroupCreateRequest(List.of(1L, 2L)));

            // when & then
            테이블_그룹_해제(1L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("계산 완료 상태가 아닌 주문이 있는 테이블은 그룹을 해제할 수 없습니다."));
        }

        @Test
        void 성공하면_204() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            테이블_그룹_생성(new TableGroupCreateRequest(List.of(1L, 2L)));

            // when & then
            테이블_그룹_해제(1L)
                .andExpect(status().isNoContent());
        }

        @Test
        void 성공하면_테이블_그룹이_존재하지_않음() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            테이블_그룹_생성(new TableGroupCreateRequest(List.of(1L, 2L)));
            테이블_그룹_해제(1L);

            // when & then
            주문_테이블_모두_조회()
                .andExpect(jsonPath("$[0].tableGroupId").doesNotExist())
                .andExpect(jsonPath("$[1].tableGroupId").doesNotExist());
        }
    }
}
