package kitchenpos.ordertable.ui;

import static kitchenpos.fixture.TableFixture.EMPTY_TABLE_REQUEST;
import static kitchenpos.fixture.TableFixture.TABLE;
import static kitchenpos.fixture.TableFixture.createTableById;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dto.OrderTableEmptyChangeRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsChangeRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
        BDDMockito.given(tableService.create(ArgumentMatchers.any())).willReturn(createTableById(1L));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(EMPTY_TABLE_REQUEST)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/api/tables/1"));
    }

    @Test
    void list() throws Exception {
        // given
        BDDMockito.given(tableService.list()).willReturn(List.of(TABLE));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tables"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(TABLE.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfGuests").value(TABLE.getNumberOfGuests()));
    }

    @Test
    void changeEmpty() throws Exception {
        // given
        final OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);
        BDDMockito.given(tableService.changeEmpty(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(TABLE);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TABLE.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfGuests").value(TABLE.getNumberOfGuests()));
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        final OrderTableNumberOfGuestsChangeRequest request = new OrderTableNumberOfGuestsChangeRequest(10);
        BDDMockito.given(tableService.changeNumberOfGuests(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .willReturn(TABLE);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TABLE.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfGuests").value(TABLE.getNumberOfGuests()));
    }
}
