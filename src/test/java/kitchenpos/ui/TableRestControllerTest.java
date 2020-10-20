package kitchenpos.ui;

import static kitchenpos.utils.TestObjects.*;
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
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("create: 새 테이블 생성 요청시, 200 상태코드 반환, 새로 생성된 테이블을 반환한다.")
    @Test
    void create() throws Exception {
        final OrderTable orderTable = createTable(null, 5, false);

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
        orderTableDao.save(createTable(null, 5, false));

        final String tableListApiUrl = "/api/tables";
        final ResultActions resultActions = findList(tableListApiUrl);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("changeEmpty: 현재 주문 테이블의 비움 처리 요청시, 200 상태코드반환, 주문 테이블 비운 처리")
    @Test
    void changeEmpty() throws Exception {
        final OrderTable savedNonEmptyTable = tableService.create(createTable(null, 0, false));
        final OrderTable emptyTable = createTable(null, 0, true);

        String updateEmptyStatusApiUrl = "/api/tables/{orderTableId}/empty";
        final Long savedTableId = savedNonEmptyTable.getId();
        final ResultActions resultActions = updateByPathIdAndBody(updateEmptyStatusApiUrl, savedTableId, emptyTable);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tableGroupId", nullValue()))
                .andExpect(jsonPath("$.numberOfGuests", is(0)))
                .andExpect(jsonPath("$.empty", is(true)));
    }

    @DisplayName("changeNumberOfGuests: 대상 테이블의 손님 수를 변경 후, 200 코드와 변경된 테이블 entity를 반환한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        final OrderTable savedNonEmptyTable = tableService.create(createTable(null, 5, false));
        final OrderTable tableWithNewNumberOfGuest = createTable(null, 10, false);

        String updateGuestCountApiUrl = "/api/tables/{orderTableId}/number-of-guests";
        final Long nonEmptyTableId = savedNonEmptyTable.getId();
        final ResultActions resultActions = updateByPathIdAndBody(updateGuestCountApiUrl, nonEmptyTableId,
                tableWithNewNumberOfGuest);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tableGroupId", nullValue()))
                .andExpect(jsonPath("$.numberOfGuests", is(10)))
                .andExpect(jsonPath("$.empty", is(false)));
    }
}