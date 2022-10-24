package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
public class TableRestControllerTest extends ControllerTest {

    @MockBean
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    public void create() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        given(tableService.create(any()))
                .willReturn(new OrderTable(1L));

        // when
        ResultActions perform = mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    public void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    public void changeEmpty() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(true);

        // when
        ResultActions perform = mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("테이블 인원 수를 변경한다.")
    @Test
    public void changeNumberOfGuests() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(4);

        // when
        ResultActions perform = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
