package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.application.dto.TableGroupRequest.OrderTableIdRequest;
import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @Test
    void 단체_지정을_생성한다() throws Exception {
        // given
        TableGroup createdTableGroup = new TableGroup(1L, LocalDateTime.now(), List.of(orderTable(10, true), orderTable(11, true)));

        // when
        when(tableGroupService.create(any(TableGroupRequest.class))).thenReturn(createdTableGroup);

        // then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new TableGroupRequest(List.of(new OrderTableIdRequest(1L))))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/table-groups/" + createdTableGroup.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(createdTableGroup)));
    }

    @Test
    void 단체_지정을_해제한다() throws Exception {
        // given
        Long tableGroupId = 1L;

        // when
        doNothing().when(tableGroupService).ungroup(tableGroupId);

        // then
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent());
    }
}

