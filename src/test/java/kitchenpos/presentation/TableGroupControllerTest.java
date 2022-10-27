package kitchenpos.presentation;

import static kitchenpos.application.fixture.dto.OrderTableDtoFixture.빈테이블_응답;
import static kitchenpos.application.fixture.dto.OrderTableDtoFixture.테이블_응답;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.request.EmptyRequest;
import kitchenpos.dto.request.NumberOfGuestsRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class TableGroupControllerTest extends ControllerTest {

    @Test
    @DisplayName("단체를 지정한다.")
    void create() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(new TableGroupRequest(List.of(1L, 2L)));
        given(tableGroupService.create(any()))
                .willReturn(new TableGroupResponse(1L, LocalDateTime.now(), List.of(빈테이블_응답, 테이블_응답)));

        // when
        final ResultActions perform = mockMvc.perform(
                        post("/api/table-groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()));
    }

    @Test
    @DisplayName("단체를 해제한다.")
    void ungroup() throws Exception {
        // when
        final ResultActions perform = mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1))
                .andDo(print());

        // then
        perform.andExpect(status().isNoContent());
    }
}
