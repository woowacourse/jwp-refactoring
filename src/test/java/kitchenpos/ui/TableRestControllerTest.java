package kitchenpos.ui;

import static kitchenpos.OrderTableFixture.*;
import static kitchenpos.ProductFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @DisplayName("정상적인 주문 테이블 생성 요청에 created 상태로 응답하는지 확인한다.")
    @Test
    void createTest() throws Exception {
        final OrderTable savedOrderTable = createOrderTableWithId(1L);
        given(tableService.create(any(OrderTable.class))).willReturn(savedOrderTable);

        mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(createOrderTableWithoutId()))
        )
            .andExpect(status().isCreated())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(savedOrderTable)))
            .andExpect(header().exists("Location"));
    }

    @DisplayName("정상적인 주문 테이블 리스트 요청에 ok상태로 응답하는지 확인한다.")
    @Test
    void listTest() throws Exception {
        final List<OrderTable> orderTables = createOrderTables();
        given(tableService.list()).willReturn(orderTables);

        mockMvc.perform(get("/api/tables"))
            .andExpect(status().isOk())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(orderTables)));
    }

    @DisplayName("정상적인 빈 테이블로 수정 요청에 ok상태로 응답하는지 확인한다.")
    @Test
    void changeEmptyTest() throws Exception {
        final OrderTable savedOrderTable = createOrderTableWithId(1L);
        savedOrderTable.setEmpty(true);
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(savedOrderTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(createOrderTableWithId(1L)))
        )
            .andExpect(status().isOk())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(savedOrderTable)));
    }

    @DisplayName("정상적인 테이블 손님 수 수정 요청에 ok상태로 응답하는지 확인한다.")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        final OrderTable savedOrderTable = createOrderTableWithId(1L);
        savedOrderTable.setNumberOfGuests(4);
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(savedOrderTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(createOrderTableWithId(1L)))
        )
            .andExpect(status().isOk())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(savedOrderTable)));
    }
}

