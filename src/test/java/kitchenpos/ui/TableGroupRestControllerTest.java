package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    private static final String BASE_URL = "/api/table-groups";

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("새로운 단체 테이블 등록")
    @Test
    void createTest() throws Exception {
        OrderTableRequest firstOrderTableRequest = new OrderTableRequest(1L);
        OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                firstOrderTableRequest, secondOrderTableRequest
        ));
        String content = objectMapper.writeValueAsString(tableGroupRequest);
        TableGroupResponse tableGroupResponse = new TableGroupResponse(1L);

        given(tableGroupService.create(any())).willReturn(tableGroupResponse);

        mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @DisplayName("단체 테이블 지정 해제")
    @Test
    void ungroupTest() throws Exception {
        doNothing().when(tableGroupService).ungroup(any());

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());
    }
}
