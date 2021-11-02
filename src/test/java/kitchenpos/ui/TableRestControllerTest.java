package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ObjectMapperForTest;
import kitchenpos.application.TableService;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.request.TableChangeEmptyRequest;
import kitchenpos.ui.request.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ObjectMapperForTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        OrderTableRequest orderTable = new OrderTableRequest(5L);
        OrderTableResponse expected = new OrderTableResponse(1L, 1L, 5, true);
        given(tableService.create(any(OrderTableRequest.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(orderTable))
        );

        //then
        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/tables/%s", expected.getId())))
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void readAll() throws Exception {
        //given
        OrderTableResponse 테이블_A = new OrderTableResponse(1L, 1L, 2, true);
        OrderTableResponse 테이블_B = new OrderTableResponse(2L, 1L, 2, false);
        OrderTableResponse 테이블_C = new OrderTableResponse(3L, 2L, 4, true);
        OrderTableResponse 테이블_D = new OrderTableResponse(4L, 2L, 6, false);
        List<OrderTableResponse> expected = Arrays.asList(테이블_A, 테이블_B, 테이블_C, 테이블_D);
        given(tableService.list()).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(get("/api/tables"));

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("빈 테이블의 상태를 변경한다.")
    @Test
    void changeEmptyTrue() throws Exception {
        //given
        Long orderTableId = 4L;
        TableChangeEmptyRequest notEmptyTable = new TableChangeEmptyRequest(false);
        OrderTableResponse expected = new OrderTableResponse(orderTableId, null, 5, false);
        given(tableService.changeEmpty(anyLong(), any(TableChangeEmptyRequest.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(put(String.format("/api/tables/%s/empty", orderTableId))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(notEmptyTable))
        );

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }


    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        //given
        Long orderTableId = 4L;
        TableChangeNumberOfGuestsRequest changeNumberOfGuestTable = new TableChangeNumberOfGuestsRequest(8);
        OrderTableResponse expected = new OrderTableResponse(orderTableId, null, 8, false);
        given(tableService.changeNumberOfGuests(anyLong(), any(TableChangeNumberOfGuestsRequest.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(put(String.format("/api/tables/%s/number-of-guests", orderTableId))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(changeNumberOfGuestTable))
        );

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }
}
