package kitchenpos.ui;

import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("테이블 그룹 문서화 테스트")
class TableGroupRestControllerTest extends ApiDocument {
    @DisplayName("테이블 그룹 생성 - 성공")
    @Test
    void table_group_create() throws Exception {
        //given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(OrderTableFixture.FIRST_EMPTY_FALSE, OrderTableFixture.SECOND_EMPTY_FALSE));
        //when
        willReturn(TableGroupFixture.FIRST_SECOND_TABLE).given(tableGroupService).create(any(TableGroup.class));
        final ResultActions result = 테이블_그룹_생성_요청(tableGroup);
        //then
        테이블_그룹_생성_성공함(result, TableGroupFixture.FIRST_SECOND_TABLE);
    }

    @DisplayName("테이블 그룹 해제 - 성공")
    @Test
    void table_ungroup() throws Exception {
        //given
        //when
        willDoNothing().given(tableGroupService).ungroup(anyLong());
        final ResultActions result = 테이블_그룹_해제_요청(TableGroupFixture.FIRST_SECOND_TABLE.getId());
        //then
        테이블_그룹_해제_성공함(result);
    }

    private ResultActions 테이블_그룹_생성_요청(TableGroup tableGroup) throws Exception {
        return mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(tableGroup))
        );
    }

    private ResultActions 테이블_그룹_해제_요청(Long id) throws Exception {
        return mockMvc.perform(delete("/api/table-groups/{tableGroupId}", id));
    }

    private void 테이블_그룹_생성_성공함(ResultActions result, TableGroup tableGroup) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(tableGroup)))
                .andDo(toDocument("table-group-create"));
    }

    private void 테이블_그룹_해제_성공함(ResultActions result) throws Exception {
        result.andExpect(status().isNoContent())
                .andDo(toDocument("table-group-ungroup"));
    }
}