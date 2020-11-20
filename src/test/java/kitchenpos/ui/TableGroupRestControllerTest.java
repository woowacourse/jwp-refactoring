package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.OrderTableRepository;

@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @MockBean
    private OrderTableRepository orderTableRepository;

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() throws Exception {
        TableGroup tableGroup = TableGroupFixture.createWithId(TableGroupFixture.ID1);
        TableGroupCreateRequest request = TableGroupFixture.createRequest();

        when(tableGroupService.create(any())).thenReturn(tableGroup);
        when(orderTableRepository.countByIdIn(anyList()))
            .thenReturn(Long.valueOf(request.getOrderTables().size()));

        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(
                header().stringValues("location", "/api/table-groups/" + tableGroup.getId()))
            .andExpect(jsonPath("id").value(tableGroup.getId()));
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() throws Exception {
        mockMvc.perform(
            delete("/api/table-groups/{tableGroupId}", TableGroupFixture.ID1)
        )
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
