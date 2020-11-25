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

@SuppressWarnings("NonAsciiCharacters")
class TableRestControllerTest extends ControllerTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("create: 새 테이블 생성 요청시, 200 상태코드 반환, 새로 생성된 테이블을 반환한다.")
    @Test
    void create() throws Exception {
        final OrderTable 손님있는테이블 = createTable(null, 5, false);

        final String 테이블추가_API_URL = "/api/tables";
        final ResultActions resultActions = create(테이블추가_API_URL, 손님있는테이블);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.tableGroupId", nullValue(Long.class)))
                .andExpect(jsonPath("$.numberOfGuests", is(5)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @DisplayName("list: 전체 테이블 목록 조회 요청시, 200 상태 코드와 함께, 전체 테이블 내역을 반환한다.")
    @Test
    void list() throws Exception {
        orderTableDao.save(createTable(null, 5, false));

        final String 테이블목록조회_API_URL = "/api/tables";
        final ResultActions resultActions = findList(테이블목록조회_API_URL);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("changeEmpty: 현재 주문 테이블의 비움 처리 요청시, 200 상태코드반환, 주문 테이블 비운 처리")
    @Test
    void changeEmpty() throws Exception {
        final OrderTable 비어있지않는테이블 = tableService.create(createTable(null, 0, false));

        String 테이블비움요청_API_URL = "/api/tables/{orderTableId}/empty";
        final Long 기존테이블식별자 = 비어있지않는테이블.getId();
        final OrderTable 변경하려는테이블상태 = createTable(null, 0, true);
        final ResultActions resultActions = updateByPathIdAndBody(테이블비움요청_API_URL, 기존테이블식별자, 변경하려는테이블상태);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.tableGroupId", nullValue(Long.class)))
                .andExpect(jsonPath("$.numberOfGuests", is(0)))
                .andExpect(jsonPath("$.empty", is(true)));
    }

    @DisplayName("changeNumberOfGuests: 대상 테이블의 손님 수를 변경 후, 200 코드와 변경된 테이블 entity를 반환한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        final OrderTable 비어있지않는테이블 = tableService.create(createTable(null, 5, false));
        final OrderTable 변경하려는테이블상태 = createTable(null, 10, false);

        String 테이블손님수변경_API_URL = "/api/tables/{orderTableId}/number-of-guests";
        final Long 기존테이블식별자 = 비어있지않는테이블.getId();
        final ResultActions resultActions = updateByPathIdAndBody(테이블손님수변경_API_URL, 기존테이블식별자,
                변경하려는테이블상태);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.tableGroupId", nullValue(Long.class)))
                .andExpect(jsonPath("$.numberOfGuests", is(10)))
                .andExpect(jsonPath("$.empty", is(false)));
    }
}