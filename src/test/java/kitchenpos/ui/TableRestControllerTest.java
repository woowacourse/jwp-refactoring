package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

class TableRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("create: 테이블 등록 테스트")
    @Test
    void createTest() throws Exception {
        final OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        create("/api/tables", table)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.numberOfGuests").value(0));
    }

    @DisplayName("list: 테이블 전체 조회 테스트")
    @Test
    void listTest() throws Exception {
        findList("/api/tables")
                .andExpect(jsonPath("$[0].numberOfGuests").value(0))
                .andExpect(jsonPath("$[1].numberOfGuests").value(0));
    }

    @DisplayName("changeEmpty: 테이블의 비어있는 상태를 변경하는 테스트")
    @Test
    void changeEmptyTest() throws Exception {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        update("/api/tables/1/empty", orderTable)
                .andExpect(jsonPath("$.empty").value(false));
    }

    @DisplayName("changeNumberOfGuests: 손님 수를 변경하는 테스")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        final OrderTable expected = new OrderTable();
        expected.setNumberOfGuests(5);

        update("/api/tables/7/number-of-guests", expected)
                .andExpect(jsonPath("$.numberOfGuests").value(5));
    }
}
