package kitchenpos.ui;

import static kitchenpos.fixture.TableFixture.EMPTY_TABLE_REQUEST;
import static kitchenpos.fixture.TableFixture.TABLE;
import static kitchenpos.fixture.TableFixture.createTableById;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.dto.OrderTableEmptyChangeRequest;
import kitchenpos.dto.OrderTableNumberOfGuestsChangeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @Test
    void create() throws Exception {
        // given
        given(tableService.create(any())).willReturn(createTableById(1L));

        // when & then
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(EMPTY_TABLE_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/tables/1"));
    }

    @Test
    void list() throws Exception {
        // given
        given(tableService.list()).willReturn(List.of(TABLE));

        // when & then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(TABLE.getId()))
                .andExpect(jsonPath("$[0].tableGroupId").value(TABLE.getTableGroupId()))
                .andExpect(jsonPath("$[0].numberOfGuests").value(TABLE.getNumberOfGuests()));
    }

    @Test
    void changeEmpty() throws Exception {
        // given
        final OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);
        given(tableService.changeEmpty(any(), any())).willReturn(TABLE);

        // when & then
        mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TABLE.getId()))
                .andExpect(jsonPath("$.tableGroupId").value(TABLE.getTableGroupId()))
                .andExpect(jsonPath("$.numberOfGuests").value(TABLE.getNumberOfGuests()));
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        final OrderTableNumberOfGuestsChangeRequest request = new OrderTableNumberOfGuestsChangeRequest(10);
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(TABLE);

        // when & then
        mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TABLE.getId()))
                .andExpect(jsonPath("$.tableGroupId").value(TABLE.getTableGroupId()))
                .andExpect(jsonPath("$.numberOfGuests").value(TABLE.getNumberOfGuests()));
    }
}
