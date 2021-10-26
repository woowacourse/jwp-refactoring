package kitchenpos.ui;

import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("테이블 문서화 테스트")
class TableRestControllerTest extends ApiDocument {
    @DisplayName("테이블 생성 - 성공")
    @Test
    void table_create() throws Exception {
        //given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        //when
        willReturn(OrderTableFixture.NINTH).given(tableService).create(any(OrderTable.class));
        final ResultActions result = 테이블_생성_요청(orderTable);
        //then
        테이블_생성_성공함(result, OrderTableFixture.NINTH);
    }

    @DisplayName("테이블 조회 - 성공")
    @Test
    void table_findAll() throws Exception {
        //given
        //when
        willReturn(OrderTableFixture.orderTables()).given(tableService).list();
        final ResultActions result = 테이블_조회_요청();
        //then
        테이블_조회_성공함(result, OrderTableFixture.orderTables());
    }

    @DisplayName("테이블 상태 변경 - 성공")
    @Test
    void table_change_status() throws Exception {
        //given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        //when
        willReturn(OrderTableFixture.FIRST_EMPTY_FALSE).given(tableService).changeEmpty(anyLong(), any(OrderTable.class));
        final ResultActions result = 테이블_상태_변경_요청(OrderTableFixture.FIRST.getId(), orderTable);
        //then
        테이블_상태_변경_성공함(result, OrderTableFixture.FIRST_EMPTY_FALSE);
    }

    @DisplayName("테이블 손님 수 변경 - 성공")
    @Test
    void table_change_guest() throws Exception {
        //given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        //when
        willReturn(OrderTableFixture.FIRST_EMPTY_FALSE_HAVE_GUEST).given(tableService).changeNumberOfGuests(anyLong(), any(OrderTable.class));
        final ResultActions result = 테이블_손님_수_변경_요청(OrderTableFixture.FIRST.getId(), orderTable);
        //then
        테이블_손님_수_변경_성공함(result, OrderTableFixture.FIRST_EMPTY_FALSE_HAVE_GUEST);
    }

    private ResultActions 테이블_생성_요청(OrderTable orderTable) throws Exception {
        return mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTable))
        );
    }

    private ResultActions 테이블_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions 테이블_상태_변경_요청(Long id, OrderTable orderTable) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/empty", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTable))
        );
    }

    private ResultActions 테이블_손님_수_변경_요청(Long id, OrderTable orderTable) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTable))
        );
    }

    private void 테이블_생성_성공함(ResultActions result, OrderTable orderTable) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(orderTable)))
                .andExpect(header().string("Location", "/api/tables/" + orderTable.getId()))
                .andDo(toDocument("table-create"));
    }

    private void 테이블_조회_성공함(ResultActions result, List<OrderTable> orderTables) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderTables)))
                .andDo(toDocument("table-findAll"));
    }

    private void 테이블_상태_변경_성공함(ResultActions result, OrderTable orderTable) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderTable)))
                .andDo(toDocument("table-change-status"));
    }

    private void 테이블_손님_수_변경_성공함(ResultActions result, OrderTable orderTable) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderTable)))
                .andDo(toDocument("table-change-quest-number"));
    }
}