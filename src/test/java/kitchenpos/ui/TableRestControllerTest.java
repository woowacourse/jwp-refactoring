package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.table.TableService;
import kitchenpos.application.dto.OrderTableCreationRequest;
import kitchenpos.application.dto.OrderTableEmptyStatusChangeRequest;
import kitchenpos.application.dto.OrderTableGuestAmountChangeRequest;
import kitchenpos.application.dto.result.OrderTableResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @Test
    void create() throws Exception {
        // given
        final OrderTableResult result = new OrderTableResult(1L, 4, false);
        given(tableService.create(any())).willReturn(result);

        final OrderTableCreationRequest request = new OrderTableCreationRequest(4, false);

        // when
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/tables/1"));
    }

    @Test
    void list() throws Exception {
        // given
        final OrderTableResult resultA = new OrderTableResult(1L, 4, false);
        final OrderTableResult resultB = new OrderTableResult(2L, 4, false);
        given(tableService.list()).willReturn(List.of(resultA, resultB));

        // when
        mockMvc.perform(get("/api/tables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(resultA, resultB))));
    }

    @Test
    void changeEmpty() throws Exception {
        // given
        final OrderTableResult result = new OrderTableResult(1L, 4, false);
        given(tableService.changeEmpty(any(), any())).willReturn(result);

        final OrderTableEmptyStatusChangeRequest request = new OrderTableEmptyStatusChangeRequest(true);

        // when
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(result)));
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        final OrderTableResult result = new OrderTableResult(1L, 4, false);
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(result);

        final OrderTableGuestAmountChangeRequest request = new OrderTableGuestAmountChangeRequest(4);

        // when
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(result)));
    }
}
