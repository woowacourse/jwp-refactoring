package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.Fixtures;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.ui.TableGroupRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() throws Exception {
        TableGroup tableGroup = Fixtures.makeTableGroup();

        ObjectMapper objectMapper = new ObjectMapper();

        String content = objectMapper.writeValueAsString(tableGroup);

        given(tableGroupService.create(any(TableGroupRequest.class)))
            .willReturn(tableGroup);

        mvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());
    }

    @DisplayName("table group 삭제")
    @Test
    void delete() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .delete("/api/table-groups/{tableGroupId}", anyLong()))
            .andExpect(status().isNoContent());
    }
}
