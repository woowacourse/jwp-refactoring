package kitchenpos.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.application.dto.TableResponse;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.ui.TableRestController;
import org.junit.jupiter.api.DisplayName;
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
    ObjectMapper objectMapper;

    @MockBean
    TableService tableService;

    @Test
    @DisplayName("POST /api/tables")
    void createProduct() throws Exception {
        when(tableService.create(new NumberOfGuests(5), false))
                .thenReturn(new TableResponse(1L, null, new NumberOfGuests(5), false));

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("numberOfGuests", 5, "empty", false))))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
