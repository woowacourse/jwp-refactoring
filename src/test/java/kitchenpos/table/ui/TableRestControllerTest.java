package kitchenpos.table.ui;

import kitchenpos.builder.OrderTableBuilder;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.BaseWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TableRestControllerTest extends BaseWebMvcTest {

    OrderTable orderTable1;
    OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTableBuilder()
                .id(1L)
                .tableGroupId(null)
                .numberOfGuests(0)
                .empty(true)
                .build();
        orderTable2 = new OrderTableBuilder()
                .id(2L)
                .tableGroupId(null)
                .numberOfGuests(0)
                .empty(true)
                .build();
    }

    @DisplayName("POST /api/tables -> 테이블을 추가한다.")
    @Test
    void create() throws Exception {
        // given
        OrderTableCreateRequest requestOrderTable = new OrderTableCreateRequest(0, true);
        String content = parseJson(requestOrderTable);

        given(tableService.create(any(OrderTable.class)))
                .willReturn(orderTable1);

        // when
        ResultActions actions = mvc.perform(postRequest("/api/tables", content))
                .andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.tableGroupId", nullValue()))
                .andExpect(jsonPath("$.numberOfGuests", is(0)))
                .andExpect(jsonPath("$.empty", is(true)))
                .andDo(print());
    }

    @DisplayName("GET /api/tables -> 테이블 전체를 조회한다.")
    @Test
    void list() throws Exception {

        // given
        given(tableService.list())
                .willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        ResultActions actions = mvc.perform(getRequest("/api/tables"))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].tableGroupId", nullValue()))
                .andExpect(jsonPath("$[0].numberOfGuests", is(0)))
                .andExpect(jsonPath("$[0].empty", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].tableGroupId", nullValue()))
                .andExpect(jsonPath("$[1].numberOfGuests", is(0)))
                .andExpect(jsonPath("$[1].empty", is(true)))
                .andDo(print());
    }

    @DisplayName("PUT /api/tables/{orderTableId}/empty -> 특정 테이블의 상태를 변경한다.")
    @Test
    void changeEmpty() throws Exception {

        // given
        OrderTable requestOrderTable = new OrderTableBuilder()
                .id(null)
                .tableGroupId(null)
                .numberOfGuests(0)
                .empty(false)
                .build();
        String content = parseJson(requestOrderTable);
        Long requestTableId = 1L;

        OrderTable orderTable = new OrderTableBuilder()
                .id(1L)
                .tableGroupId(null)
                .numberOfGuests(0)
                .empty(false)
                .build();
        given(tableService.changeEmpty(any(Long.class), any(OrderTable.class)))
                .willReturn(orderTable);

        // when
        ResultActions actions = mvc.perform(putRequest("/api/tables/{orderTableId}/empty", content, requestTableId))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.empty", is(false)))
                .andDo(print());
    }

    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests -> 테이블의 인원수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {

        // given
        OrderTable requestOrderTable = new OrderTableBuilder()
                .id(null)
                .tableGroupId(null)
                .numberOfGuests(4)
                .empty(false)
                .build();
        String content = parseJson(requestOrderTable);
        Long requestTableId = 1L;

        OrderTable orderTable = new OrderTableBuilder()
                .id(1L)
                .tableGroupId(null)
                .numberOfGuests(4)
                .empty(false)
                .build();
        given(tableService.changeNumberOfGuests(any(Long.class), any(OrderTable.class)))
                .willReturn(orderTable);

        // when
        ResultActions actions = mvc.perform(putRequest("/api/tables/{orderTableId}/number-of-guests", content, requestTableId))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests", is(4)))
                .andDo(print());
    }
}