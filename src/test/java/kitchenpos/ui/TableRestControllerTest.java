package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
        final OrderTable orderTableDto = new OrderTable();
        final String content = objectMapper.writeValueAsString(orderTableDto);
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        when(tableService.create(any())).thenReturn(orderTable);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/tables")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.getHeader("Location")).isEqualTo("/api/tables/1")
        );
    }

    @Test
    void list() throws Exception {
        when(tableService.list()).thenReturn(Collections.singletonList(new OrderTable()));

        final MockHttpServletResponse response = mockMvc.perform(get("/api/tables"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void changeEmpty() throws Exception {
        final OrderTable orderTableDto = new OrderTable();
        final String content = objectMapper.writeValueAsString(orderTableDto);
        when(tableService.changeEmpty(any(), any())).thenReturn(new OrderTable());

        final MockHttpServletResponse response = mockMvc.perform(put("/api/tables/1/empty")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        final OrderTable orderTableDto = new OrderTable();
        final String content = objectMapper.writeValueAsString(orderTableDto);
        when(tableService.changeNumberOfGuests(any(), any())).thenReturn(new OrderTable());

        final MockHttpServletResponse response = mockMvc.perform(put("/api/tables/1/empty")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
