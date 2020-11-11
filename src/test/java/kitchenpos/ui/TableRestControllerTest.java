package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest {
    @MockBean
    private TableService tableService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("테이블을 생성한다")
    void create() throws Exception {
        OrderTable request = createOrderTableRequest(2, true);
        byte[] content = objectMapper.writeValueAsBytes(request);
        given(tableService.create(any(OrderTable.class)))
                .willReturn(createOrderTable(7L, request.isEmpty(), null, request.getNumberOfGuests()));

        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.numberOfGuests").value(2))
                .andExpect(jsonPath("$.empty").value(true));
    }

    @Test
    @DisplayName("전체 테이블을 조회한다")
    void list() throws Exception {
        List<OrderTable> persistedOrderTables = Arrays.asList(
                createOrderTable(1L, false, null, 2),
                createOrderTable(2L, false, 2L, 2),
                createOrderTable(3L, true, 2L, 2),
                createOrderTable(4L, false, null, 2)
        );
        given(tableService.list()).willReturn(persistedOrderTables);

        byte[] responseBody = mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        List<OrderTable> result = objectMapper.readValue(responseBody, new TypeReference<List<OrderTable>>() {
        });
        assertThat(result).usingRecursiveComparison().isEqualTo(persistedOrderTables);
    }

    @Test
    @DisplayName("주문 테이블의 빈 테이블 여부를 수정한다")
    void updateIsEmpty() throws Exception {
        OrderTable request = modifyOrderTableEmptyRequest(true);
        byte[] content = objectMapper.writeValueAsBytes(request);
        given(tableService.changeEmpty(eq(7L), any(OrderTable.class)))
                .willReturn(createOrderTable(7L, request.isEmpty(), 3L, 2));

        mockMvc.perform(put("/api/tables/{id}/empty", 7L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.empty").value(true));
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 수정한다")
    void updateNumberOfGuests() throws Exception {
        OrderTable request = modifyOrderTableNumOfGuestRequest(4);
        byte[] content = objectMapper.writeValueAsBytes(request);
        given(tableService.changeNumberOfGuests(eq(7L), any(OrderTable.class)))
                .willReturn(createOrderTable(7L, false, 3L, request.getNumberOfGuests()));

        mockMvc.perform(put("/api/tables/{id}/number-of-guests", 7L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.numberOfGuests").value(4));
    }
}