package kitchenpos.ui;

import static kitchenpos.ProductFixture.*;
import static kitchenpos.TableGroupFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @DisplayName("정상적인 단체 지정 생성 요청에 created 상태로 응답하는지 확인한다.")
    @Test
    void createTest() throws Exception {
        final TableGroup savedTableGroup = createTableGroupWithId(1L);
        given(tableGroupService.create(any(TableGroup.class))).willReturn(savedTableGroup);

        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(createProductWithoutId()))
        )
            .andExpect(status().isCreated())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(savedTableGroup)))
            .andExpect(header().exists("Location"));
    }

    @DisplayName("정상적인 단체 지정 해제 요청에 noContent 상태로 응답하는지 확인한다.")
    @Test
    void ungroupTest() throws Exception {
        doNothing().when(tableGroupService).ungroup(1L);

        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1L))
            .andExpect(status().isNoContent());
    }
}

