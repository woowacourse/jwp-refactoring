package kitchenpos.ui.v1.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineCreateRequest;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderControllerV1IntegrationTest extends V1IntegrationTest {

    @Nested
    class create {

        @Test
        void 주문_항목_목록이_없으면_400() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 1000));
            메뉴_생성(new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(1, 1L)
            )));

            var request = new OrderCreateRequest(1L, Collections.emptyList());

            // when & then
            주문_생성(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("주문 목록 항목은 비어있을 수 없습니다."));
        }

        @Test
        void 성공하면_201() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            메뉴_그룹_생성(new MenuGroupCreateRequest("주류"));
            상품_생성(new ProductCreateRequest("맥주", 1000));
            메뉴_생성(new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(1, 1L)
            )));

            var request = new OrderCreateRequest(1L, List.of(
                new OrderLineCreateRequest(1L, 3)
            ));

            // when & then
            주문_생성(request)
                .andExpect(status().isCreated());
        }

        @Test
        void 주문을_하면_주문의_상태는_조리중() throws Exception {
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

            // when & then
            모든_주문_조회()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("COOKING"));
        }
    }

    @Nested
    class changeOrderStatus {

        @Test
        void 계산_완료된_주문을_변경하면_400() throws Exception {
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

            // when & then
            var request = new OrderUpdateRequest(OrderStatus.MEAL);

            // when & then
            주문_상태_변경(1L, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("계산이 완료된 주문은 상태를 변경할 수 없습니다."));
        }

        @Test
        void 성공하면_200() throws Exception {
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

            var request = new OrderUpdateRequest(OrderStatus.MEAL);

            // when & then
            주문_상태_변경(1L, request)
                .andExpect(status().isOk());
        }

        @Test
        void 성공하면_상태가_변경() throws Exception {
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
            주문_상태_변경(1L, new OrderUpdateRequest(OrderStatus.MEAL));

            // when & then
            모든_주문_조회()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("MEAL"));
        }
    }
}
