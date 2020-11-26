package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.table.response.OrderTableResponse;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.ui.table.TableRestController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithNumberOfGuest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
@AutoConfigureMockMvc
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("Table 생성 요청")
    @Test
    void createOrderLineItems() throws Exception {
        OrderTable orderTable = createOrderTableWithId(null);
        String content = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(orderTable);
        given(tableService.create(any())).willReturn(createOrderTableWithId(1L));

        mockMvc.perform(post("/api/tables")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/tables/1"));
    }

    @DisplayName("Table 전체 조회 요청")
    @Test
    void list() throws Exception {
        OrderTable orderTable = OrderTableFixture.createOrderTableWithId(1L);
        OrderTableResponse orderTableResponse = OrderTableResponse.from(orderTable);
        given(tableService.list())
                .willReturn(Arrays.asList(orderTableResponse));

        mockMvc.perform(get("/api/tables")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)));
    }

    @DisplayName("Table 비어있는지 여부 변경")
    @Test
    void changeEmpty() throws Exception {
        OrderTable orderTable = createOrderTableWithEmpty(true);
        String content = new ObjectMapper().writeValueAsString(orderTable);
        given(tableService.changeEmpty(anyLong(), any())).willReturn(createOrderTableWithEmpty(true));

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", "1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("empty", Matchers.is(true)));

    }

    @DisplayName("Table 손님 수 변경")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable orderTable = createOrderTableWithNumberOfGuest(77);
        String content = new ObjectMapper().writeValueAsString(orderTable);
        given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(createOrderTableWithNumberOfGuest(77));

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", "1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfGuests", Matchers.is(77)));

    }
}