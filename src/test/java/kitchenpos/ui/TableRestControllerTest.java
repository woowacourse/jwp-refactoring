package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

class TableRestControllerTest extends ControllerTest {
    @Autowired
    TableService tableService;

    @Autowired
    OrderTableDao orderTableDao;

    @DisplayName("create: 새 테이블 생성 요청시, 200 상태코드 반환, 새로 생성된 테이블을 반환한다.")
    @Test
    void create() throws Exception {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);

        final String createTableApiUrl = "/api/tables";
        final ResultActions resultActions = create(createTableApiUrl, orderTable);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tableGroupId", nullValue()))
                .andExpect(jsonPath("$.numberOfGuests", is(5)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @DisplayName("list: 전체 테이블 목록 조회 요청시, 200 상태 코드와 함께, 전체 테이블 내역을 반환한다.")
    @Test
    void list() throws Exception {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        orderTableDao.save(orderTable);

        final String tableListApiUrl = "/api/tables";
        final ResultActions resultActions = findList(tableListApiUrl);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(9)));
    }

    @DisplayName("changeEmpty: 현재 주문 테이블의 비움 처리 요청시, 200 상태코드반환, 주문 테이블 비운 처리")
    @Test
    void changeEmpty() throws Exception {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        final OrderTable savedTable = tableService.create(orderTable);
        final OrderTable emptyTable = new OrderTable();
        final Long savedTableId = savedTable.getId();

        String updateEmptyStatusApiUrl = "/api/tables/{orderTableId}/empty";
        final ResultActions resultActions = updateByPathIdAndBody(updateEmptyStatusApiUrl, savedTableId, emptyTable);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tableGroupId", nullValue()))
                .andExpect(jsonPath("$.numberOfGuests", is(0)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @DisplayName("changeNumberOfGuests: 대상 테이블의 손님 수를 변경 후, 200 코드와 변경된 테이블 entity를 반환한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = tableService.create(orderTable);
        final Long savedTableId = savedTable.getId();

        final OrderTable tableWithNewNumberOfGuest = new OrderTable();
        tableWithNewNumberOfGuest.setNumberOfGuests(10);

        String updateGuestCountApiUrl = "/api/tables/{orderTableId}/number-of-guests";
        final ResultActions resultActions = updateByPathIdAndBody(updateGuestCountApiUrl, savedTableId, tableWithNewNumberOfGuest);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tableGroupId", nullValue()))
                .andExpect(jsonPath("$.numberOfGuests", is(10)))
                .andExpect(jsonPath("$.empty", is(false)));
    }
}