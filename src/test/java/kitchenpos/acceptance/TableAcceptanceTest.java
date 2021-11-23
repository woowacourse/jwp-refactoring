package kitchenpos.acceptance;

import kitchenpos.AcceptanceTest;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeGuestRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 생성 테스트 - 성공")
    public void create() throws Exception {
        //given
        OrderTableCreateRequest 주문_테이블1_생성_요청 = orderTableFixture.주문_테이블_생성_요청(2, true);

        //when
        OrderTableResponse 주문_테이블1_응답 = 주문_테이블_등록(주문_테이블1_생성_요청);

        //then
        assertThat(주문_테이블1_응답).usingRecursiveComparison()
                .isEqualTo(OrderTableResponse.create(orderTableFixture.주문_테이블_조회(주문_테이블1_응답.getId())));
    }

    @Test
    @DisplayName("전체 테이블 조회 - 성공")
    public void list() throws Exception {
        //given
        OrderTableCreateRequest 주문_테이블1_생성_요청 = orderTableFixture.주문_테이블_생성_요청(2, true);
        OrderTableCreateRequest 주문_테이블2_생성_요청 = orderTableFixture.주문_테이블_생성_요청(4, true);
        List<OrderTableResponse> expected = orderTableFixture.주문_테이블_응답_리스트_생성(주문_테이블_등록(주문_테이블1_생성_요청), 주문_테이블_등록(주문_테이블2_생성_요청));

        //when
        List<OrderTableResponse> actual = 주문_테이블_리스트_조회();

        //then
        assertThat(actual).hasSize(expected.size());
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("테이블의 상태 변경 - 성공")
    public void changeEmpty() throws Exception {
        //given
        OrderTableCreateRequest 주문_테이블1_생성_요청 = orderTableFixture.주문_테이블_생성_요청(2, true);
        OrderTableResponse 주문_테이블1_응답 = 주문_테이블_등록(주문_테이블1_생성_요청);
        OrderTableChangeEmptyRequest 손님_착석_요청 = orderTableFixture.주문_테이블_Empty_Change_요청(false);

        //when
        OrderTableResponse 주문_테이블_착석 = 주문_테이블_착석(주문_테이블1_응답.getId(), 손님_착석_요청);

        //then
        assertEquals(주문_테이블_착석.isEmpty(), false);
    }

    @Test
    @DisplayName("식사 인원 변경 테스트 - 성공")
    public void changeNumberOfGuests() throws Exception {
        //given
        OrderTableCreateRequest 주문_테이블1_생성_요청 = orderTableFixture.주문_테이블_생성_요청(2, false);
        OrderTableResponse 주문_테이블1_응답 = 주문_테이블_등록(주문_테이블1_생성_요청);
        OrderTableChangeGuestRequest 인원_변경_요청 = orderTableFixture.주문_테이블_인원_변경_요청(5);

        //when
        OrderTableResponse 주문_테이블_인원_변경 = 주문_테이블_인원_변경(주문_테이블1_응답.getId(), 인원_변경_요청);

        //then
        assertEquals(주문_테이블_인원_변경.getNumberOfGuests(), 5);
    }
}
