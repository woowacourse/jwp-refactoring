package kitchenpos.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.OrderTable;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("TableRestController 단위 테스트")
class TableRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("테이블을 생성할 수 있다.")
    void create() throws Exception {
        // given
        OrderTable table = new OrderTable(0, true);
        OrderTable expected = new OrderTable(1L, null, 0, true);
        given(tableService.create(any(OrderTable.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(post("/api/tables")
                .content(objectToJsonString(table))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/tables/" + expected.getId()))
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("전체 테이블을 조회할 수 있다.")
    void list() throws Exception {
        // given
        OrderTable table1 = new OrderTable(1L, 1L, 3, false);
        OrderTable table2 = new OrderTable(2L, null, 0, true);
        List<OrderTable> expected = Arrays.asList(table1, table2);
        given(tableService.list()).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(get("/api/tables")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("테이블이 비어있는지를 나타내는 상태를 수정할 수 있다.")
    void changeEmpty() throws Exception {
        // given
        OrderTable changeEmptyTable = new OrderTable(null, null, 0, false);
        OrderTable expected = new OrderTable(1L, null, 0, false);
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/1/empty")
                .content(objectToJsonString(changeEmptyTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블은 존재해야 한다.")
    void changeEmptyWrongNotExist() throws Exception {
        // given
        OrderTable changeEmptyTable = new OrderTable(null, null, 0, false);
        willThrow(new IllegalArgumentException("주문 테이블이 존재하지 않습니다."))
                .given(tableService).changeEmpty(anyLong(), any(OrderTable.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/10/empty")
                .content(objectToJsonString(changeEmptyTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("주문 테이블이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블은 그룹에 속해있지 않아야한다.")
    void changeEmptyWrongNotInGroup() throws Exception {
        // given
        OrderTable changeEmptyTable = new OrderTable(null, 1L, 0, false);
        willThrow(new IllegalArgumentException("주문 테이블이 그룹에 속해있습니다. 그룹을 해제해주세요."))
                .given(tableService).changeEmpty(anyLong(), any(OrderTable.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/1/empty")
                .content(objectToJsonString(changeEmptyTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("주문 테이블이 그룹에 속해있습니다. 그룹을 해제해주세요."));
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블이 조리중(COOKING)이나 식사중(MEAL)이 아니어야한다.")
    void changeEmptyWrongCookingOrMeal() throws Exception {
        // given
        OrderTable changeEmptyTable = new OrderTable(null, null, 0, false);
        willThrow(new IllegalArgumentException("조리중이나 식사중인 경우 상태를 변경할 수 없습니다."))
                .given(tableService).changeEmpty(anyLong(), any(OrderTable.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/1/empty")
                .content(objectToJsonString(changeEmptyTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("조리중이나 식사중인 경우 상태를 변경할 수 없습니다."));
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTable changeNumberTable = new OrderTable(null, null, 5, false);
        OrderTable expected = new OrderTable(1L, null, 5, false);
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/1/number-of-guests")
                .content(objectToJsonString(changeNumberTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 기존 손님의 수는 0 이상이어야 한다.")
    void changeNumberOfGuestsWrongNumber() throws Exception {
        // given
        OrderTable changeNumberTable = new OrderTable(null, null, -1, false);
        willThrow(new IllegalArgumentException("변경하려는 손님 수는 0이상이어야 합니다."))
                .given(tableService).changeNumberOfGuests(anyLong(), any(OrderTable.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/1/number-of-guests")
                .content(objectToJsonString(changeNumberTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("변경하려는 손님 수는 0이상이어야 합니다."));
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 테이블은 존재해야 한다.")
    void changeNumberOfGuestsWrongTableNotExist() throws Exception {
        // given
        OrderTable changeNumberTable = new OrderTable(null, null, 5, false);
        willThrow(new IllegalArgumentException("주문 테이블이 존재하지 않습니다."))
                .given(tableService).changeNumberOfGuests(anyLong(), any(OrderTable.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/10/number-of-guests")
                .content(objectToJsonString(changeNumberTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("주문 테이블이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 테이블은 비어있지 않아야한다.")
    void changeNumberOfGuestsWrongTableEmpty() throws Exception {
        // given
        OrderTable changeNumberTable = new OrderTable(null, null, 5, false);
        willThrow(new IllegalArgumentException("비어있는 테이블의 손님 수를 변경할 수 없습니다."))
                .given(tableService).changeNumberOfGuests(anyLong(), any(OrderTable.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/tables/1/number-of-guests")
                .content(objectToJsonString(changeNumberTable))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("비어있는 테이블의 손님 수를 변경할 수 없습니다."));
    }

}
