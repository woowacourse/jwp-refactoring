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
import kitchenpos.Constructor;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends Constructor {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        OrderTable orderTable = orderTableConstructor(5);
        OrderTable expected = orderTableConstructor(1L, 1L, 5, true);
        given(tableService.create(any(OrderTable.class))).willReturn(expected);

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
        OrderTable 테이블_A = orderTableConstructor(1L, 1L, 2, true);
        OrderTable 테이블_B = orderTableConstructor(2L, 1L, 2, false);
        OrderTable 테이블_C = orderTableConstructor(3L, 2L, 4, true);
        OrderTable 테이블_D = orderTableConstructor(4L, 2L, 6, false);
        List<OrderTable> expected = Arrays.asList(테이블_A, 테이블_B, 테이블_C, 테이블_D);
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
        OrderTable notEmptyTable = orderTableConstructor(5, false);
        OrderTable expected = orderTableConstructor(orderTableId, null, 5, false);
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(expected);

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
        OrderTable changeNumberOfGuestTable = orderTableConstructor(8, false);
        OrderTable expected = orderTableConstructor(orderTableId, null, 8, false);
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(expected);

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
