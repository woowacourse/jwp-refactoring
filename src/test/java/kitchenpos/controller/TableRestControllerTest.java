package kitchenpos.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.Fixtures;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.ui.TableRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TableService tableService;

    @MockBean
    private TableGroupRepository tableGroupRepository;

    @DisplayName("table 생성")
    @Test
    void create() throws Exception {
        OrderTable orderTable = Fixtures.makeOrderTable();
        TableGroup tableGroup = Fixtures.makeTableGroup();

        ObjectMapper objectMapper = new ObjectMapper();

        String content = objectMapper.writeValueAsString(orderTable);

        given(tableGroupRepository.findById(anyLong()))
            .willReturn(Optional.of(tableGroup));
        given(tableService.create(any(OrderTableRequest.class)))
            .willReturn(orderTable);

        mvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());
    }

    @DisplayName("table 불러오기")
    @Test
    void list() throws Exception {
        List<OrderTable> orderTables = new ArrayList<>();

        given(tableService.list())
            .willReturn(orderTables);

        mvc.perform(get("/api/tables")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

}
