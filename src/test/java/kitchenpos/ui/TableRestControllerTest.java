package kitchenpos.ui;

import static kitchenpos.fixture.domain.OrderTableFixture.createOrderTable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.application.TableService;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.response.TableCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ControllerTest {

    @MockBean
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        OrderTable orderTable = createOrderTable(0, true);
        given(tableService.create(any())).willReturn(TableCreateResponse.from(createOrderTable(1L)));

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
    void list() throws Exception {
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
    void changeEmpty() throws Exception {
        // given
        OrderTable orderTable = createOrderTable(true);

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
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTable orderTable = createOrderTable(4);

        // when
        ResultActions perform = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("테이블 인원 수를 변경할 때 음수이면 에러를 반환한다.")
    @Test
    void changeNumberOfGuests_fail_if_numberOfGuests_is_negative() throws Exception {
        // given
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(-1);

        // when
        ResultActions perform = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isBadRequest());
    }
}
