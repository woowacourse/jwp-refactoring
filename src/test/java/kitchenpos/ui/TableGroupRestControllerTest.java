package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
@AutoConfigureMockMvc
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("TableGroup 생성 요청")
    @Test
    void createOrderLineItems() throws Exception {
        List<OrderTableRequest> orderTables = Arrays.asList(
                new OrderTableRequest(1L), new OrderTableRequest(2L));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);
        String content = new ObjectMapper().writeValueAsString(tableGroupRequest);
        given(tableGroupService.create(any())).willReturn(createTableGroupWithId(1L));

        mockMvc.perform(post("/api/table-groups")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/table-groups/1"));
    }

    @DisplayName("TableGroup 해제")
    @Test
    void ungroup() throws Exception {
        doNothing().when(tableGroupService).ungroup(1L);

        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}