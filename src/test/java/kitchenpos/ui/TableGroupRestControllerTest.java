package kitchenpos.ui;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dto.request.table.CreateTableGroupRequest;
import kitchenpos.dto.request.table.TableIdRequest;
import kitchenpos.dto.response.table.TableInGroupResponse;
import kitchenpos.dto.response.table.TableGroupResponse;

import static kitchenpos.fixture.OrderTableFixture.단일_손님0_테이블1;
import static kitchenpos.fixture.OrderTableFixture.단일_손님0_테이블2;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("TableGroupRestController 단위 테스트")
class TableGroupRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("테이블을 묶어 그룹을 지정할 수 있다 - 그룹이 지정된 테이블들은 비어 있지 않은 상태가 된다.")
    void create() throws Exception {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(
                Arrays.asList(new TableIdRequest(단일_손님0_테이블1.getId()), new TableIdRequest(단일_손님0_테이블2.getId()))
        );

        TableGroupResponse expected = new TableGroupResponse(
                1L,
                LocalDateTime.now(),
                Arrays.asList(TableInGroupResponse.from(단일_손님0_테이블1), TableInGroupResponse.from(단일_손님0_테이블2))
        );

        given(tableGroupService.create(any(CreateTableGroupRequest.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(post("/api/table-groups")
                .content(objectToJsonString(group))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/table-groups/" + expected.getId()))
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("그룹에 둘 이상의 테이블이 포함되어야한다.")
    void createWrongTableInsufficientTable() throws Exception {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(Collections.singletonList(new TableIdRequest(1L)));
        willThrow(new IllegalArgumentException("그룹을 지정하려면 둘 이상의 테이블이 필요합니다."))
                .given(tableGroupService).create(any(CreateTableGroupRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/table-groups/")
                .content(objectToJsonString(group))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("그룹을 지정하려면 둘 이상의 테이블이 필요합니다."));
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 등록된 테이블이여야 한다.")
    void createWrongTableNotRegister() throws Exception {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(
                Arrays.asList(new TableIdRequest(단일_손님0_테이블1.getId()), new TableIdRequest(단일_손님0_테이블2.getId()))
        );
        willThrow(new IllegalArgumentException("등록되지 않은 테이블은 그룹으로 지정할 수 없습니다."))
                .given(tableGroupService).create(any(CreateTableGroupRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/table-groups/")
                .content(objectToJsonString(group))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("등록되지 않은 테이블은 그룹으로 지정할 수 없습니다."));
    }

    @Test
    @DisplayName("목록에 포함된 테이블들은 모두 비어있어야 하고 모두 소속된 다른 그룹이 없어야한다.")
    void createWrongTableNotEmpty() throws Exception {
        // given
        CreateTableGroupRequest group = new CreateTableGroupRequest(
                Arrays.asList(new TableIdRequest(단일_손님0_테이블1.getId()), new TableIdRequest(단일_손님0_테이블2.getId()))
        );
        willThrow(new IllegalArgumentException("테이블이 비어있지 않거나 이미 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다."))
                .given(tableGroupService).create(any(CreateTableGroupRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/table-groups/")
                .content(objectToJsonString(group))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("테이블이 비어있지 않거나 이미 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다."));
    }

    @Test
    @DisplayName("묶여있는 테이블 그룹을 해제할 수 있다. - 그룹이 해제된 테이블들은 비어 있지 않은 상태가 된다.")
    void ungroup() throws Exception {
        // given
        Long groupId = 1L;
        willDoNothing().given(tableGroupService).ungroup(groupId);

        // when
        ResultActions response = mockMvc.perform(delete("/api/table-groups/1"));

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("목록에 포함된 테이블들의 상태가 하나라도 조리중(COOKING)이나 식사중(MEAL)인 경우 그룹을 해제할 수 없다.")
    void ungroupWrongTableCookingOrMeal() throws Exception {
        // given
        willThrow(new IllegalArgumentException("주문 상태가 조리중이나 식사중입니다."))
                .given(tableGroupService).ungroup(anyLong());

        // when
        ResultActions response = mockMvc.perform(delete("/api/table-groups/1"));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("주문 상태가 조리중이나 식사중입니다."));
    }

}
