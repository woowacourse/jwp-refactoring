package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ObjectMapperForTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.request.TableGroupRequest;
import kitchenpos.ui.response.OrderTableResponse;
import kitchenpos.ui.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ObjectMapperForTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("단체석을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        OrderTableRequest tableA = new OrderTableRequest(1L);
        OrderTableRequest tableB = new OrderTableRequest(2L);
        OrderTableRequest tableC = new OrderTableRequest(3L);
        List<OrderTableRequest> orderTables = Arrays.asList(tableA, tableB, tableC);
        TableGroupRequest tableGroup = new TableGroupRequest(orderTables);
        List<OrderTableResponse> collect = orderTables.stream()
            .map(value -> new OrderTableResponse(value.getId(), 1L, 4, true))
            .collect(Collectors.toList());
        TableGroupResponse expected = new TableGroupResponse(1L, LocalDateTime.now().toString(), collect);
        given(tableGroupService.create(any(TableGroupRequest.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(tableGroup))
        );

        //then
        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/table-groups/%s", expected.getId())))
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("단체석을 해제한다.")
    @Test
    void ungroup() throws Exception {
        //given
        Long tableGroupId = 10L;
        willDoNothing().given(tableGroupService).ungroup(tableGroupId);

        //when
        ResultActions response = mockMvc.perform(delete(String.format("/api/table-groups/%s", tableGroupId)));

        //then
        response.andExpect(status().isNoContent());
    }
}
