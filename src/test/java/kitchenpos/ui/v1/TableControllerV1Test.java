package kitchenpos.ui.v1;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
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
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@WebMvcTest(TableControllerV1.class)
class TableControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TableService tableService;

    @Test
    @DisplayName("/api/v1/tables로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        OrderTable request = new OrderTable();
        OrderTable response = new OrderTable();
        response.setId(1L);
        given(tableService.create(any(OrderTable.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/v1/tables/1"));
    }

    @Test
    @DisplayName("/api/v1/tables로 GET 요청을 보내면 200 응답과 결과가 조회된다.")
    void findAll_with_200() throws Exception {
        // given
        given(tableService.findAll())
            .willReturn(List.of(new OrderTable(), new OrderTable()));

        // when & then
        mockMvc.perform(get("/api/v1/tables")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("/api/v1/tables/{id}/empty로 PUT 요청을 보내면 200 응답이 반환된다.")
    void changeEmpty_with_200() throws Exception {
        // given
        Long tableId = 1L;
        OrderTable request = new OrderTable();
        OrderTable response = new OrderTable();
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/tables/{id}/empty", tableId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/api/v1/tables/{id}/number-of-guests로 PUT 요청을 보내면 200 응답이 반환된다.")
    void changeNumberOfGuests_with_200() throws Exception {
        // given
        Long tableId = 1L;
        OrderTable request = new OrderTable();
        OrderTable response = new OrderTable();
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/tables/{id}/number-of-guests", tableId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
