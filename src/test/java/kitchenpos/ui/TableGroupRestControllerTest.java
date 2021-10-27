package kitchenpos.ui;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.factory.OrderTableBuilder;
import kitchenpos.ui.factory.TableGroupBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TableGroupRestControllerTest extends BaseWebMvcTest {

    OrderTable orderTable1;
    OrderTable orderTable2;

    TableGroup tableGroup1;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTableBuilder()
                .id(1L)
                .tableGroupId(1L)
                .numberOfGuests(4)
                .empty(false)
                .build();

        orderTable2 = new OrderTableBuilder()
                .id(2L)
                .tableGroupId(1L)
                .numberOfGuests(4)
                .empty(false)
                .build();

        tableGroup1 = new TableGroupBuilder()
                .id(1L)
                .createdDate(LocalDateTime.now())
                .orderTables(Arrays.asList(orderTable1, orderTable2))
                .build();
    }

    @DisplayName("POST /api/table-groups -> 테이블들을 그룹화한다.")
    @Test
    void create() throws Exception {

        // given
        OrderTable requestOrderTable1 = new OrderTableBuilder()
                .id(1L)
                .build();
        OrderTable requestOrderTable2 = new OrderTableBuilder()
                .id(2L)
                .build();
        TableGroup requestTableGroup = new TableGroupBuilder()
                .id(null)
                .createdDate(null)
                .orderTables(Arrays.asList(requestOrderTable1, requestOrderTable2))
                .build();
        String content = parseJson(requestTableGroup);

        given(tableGroupService.create(any(TableGroup.class)))
                .willReturn(tableGroup1);

        // when
        ResultActions actions = mvc.perform(postRequest("/api/table-groups", content))
                .andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.createdDate", is(tableGroup1.getCreatedDate().toString())))
                .andExpect(jsonPath("$.orderTables[0].id", is(1)))
                .andExpect(jsonPath("$.orderTables[0].tableGroupId", is(1)))
                .andExpect(jsonPath("$.orderTables[0].numberOfGuests", is(4)))
                .andExpect(jsonPath("$.orderTables[0].empty", is(false)))
                .andExpect(jsonPath("$.orderTables[1].id", is(2)))
                .andExpect(jsonPath("$.orderTables[1].tableGroupId", is(1)))
                .andExpect(jsonPath("$.orderTables[1].numberOfGuests", is(4)))
                .andExpect(jsonPath("$.orderTables[1].empty", is(false)))
                .andDo(print());
    }

    @DisplayName("DELETE /api/table-groups/{tableGroupId} -> 테이블 그룹화를 해제한다.")
    @Test
    void ungroup() throws Exception {

        // given
        doNothing().when(tableGroupService).ungroup(any(Long.class));

        Long requestTableGroupId = 1L;

        // when
        ResultActions actions = mvc.perform(deleteRequest("/api/table-groups/{tableGroupId}", requestTableGroupId))
                .andDo(print());

        // then
        actions.andExpect(status().isNoContent());
    }
}