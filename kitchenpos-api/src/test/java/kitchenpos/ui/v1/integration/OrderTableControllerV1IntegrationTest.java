package kitchenpos.ui.v1.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineCreateRequest;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableUpdateRequest;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderTableControllerV1IntegrationTest extends V1IntegrationTest {

    @Nested
    class create {

        @Test
        void 성공하면_201() throws Exception {
            // given
            var request = new OrderTableCreateRequest(false, 0);

            // when & then
            주문_테이블_생성(request)
                .andExpect(status().isCreated());
        }
    }

    @Nested
    class changeEmpty {

        @Test
        void 주문_상태가_계산_완료가_아니면_400() throws Exception {
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

            var request = new OrderTableUpdateRequest(true, 0);

            // when & then
            주문_테이블_빈_테이블_변경(1L, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("계산 완료 상태가 아닌 주문이 있는 테이블은 상태를 변경할 수 없습니다. orderTableId=1"));
        }

        @Test
        void 테이블_그룹에_등록되어_있으면_400() throws Exception {
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

            주문_테이블_생성(new OrderTableCreateRequest(false, 0));
            테이블_그룹_생성(new TableGroupCreateRequest(List.of(1L, 2L)));

            var request = new OrderTableUpdateRequest(true, 0);

            // when & then
            주문_테이블_빈_테이블_변경(1L, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("테이블 그룹에 속한 테이블은 상태를 변경할 수 없습니다."));
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
            주문_상태_변경(1L, new OrderUpdateRequest(OrderStatus.COMPLETION));

            var request = new OrderTableUpdateRequest(true, 0);

            // when & then
            주문_테이블_빈_테이블_변경(1L, request)
                .andExpect(status().isOk());
        }
    }

    @Nested
    class changeNumberOfGuests {

        @Test
        void 테이블이_비어있으면_400() throws Exception {
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

            var request = new OrderTableUpdateRequest(false, 10);

            // when & then
            주문_테이블_방문자_변경(1L, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비어있는 상태의 테이블은 방문한 손님 수를 변경할 수 없습니다."));
        }

        @Test
        void 방문수가_음수이면_400() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));

            var request = new OrderTableUpdateRequest(false, -1);

            // when & then
            주문_테이블_방문자_변경(1L, request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("방문한 손님 수는 음수일 수 없습니다."));
        }

        @Test
        void 성공하면_200() throws Exception {
            // given
            주문_테이블_생성(new OrderTableCreateRequest(false, 0));

            var request = new OrderTableUpdateRequest(false, 10);

            // when & then
            주문_테이블_방문자_변경(1L, request)
                .andExpect(status().isOk());
        }
    }
}
