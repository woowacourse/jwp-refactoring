package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableChangeEmptyRequest;
import kitchenpos.dto.TableChangeGuestsRequest;
import kitchenpos.dto.TableCreateRequest;

class TableRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("create: 테이블 등록 테스트")
    @Test
    void createTest() throws Exception {
        final TableCreateRequest table = new TableCreateRequest(0, true);

        create("/api/tables", table)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.numberOfGuests").value(0))
                .andExpect(jsonPath("$.empty").value(true));
    }

    @DisplayName("list: 테이블 전체 조회 테스트")
    @Test
    void listTest() throws Exception {
        orderTableRepository.save(new OrderTable(0, true));
        orderTableRepository.save(new OrderTable(0, true));

        findList("/api/tables")
                .andExpect(jsonPath("$[0].numberOfGuests").value(0))
                .andExpect(jsonPath("$[1].numberOfGuests").value(0));
    }

    @DisplayName("changeEmpty: 테이블의 비어있는 상태를 변경하는 테스트")
    @Test
    void changeEmptyTest() throws Exception {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable saved = orderTableRepository.save(orderTable);
        final TableChangeEmptyRequest tableChangeEmptyRequest = new TableChangeEmptyRequest(false);

        update("/api/tables/" + saved.getId() + "/empty", tableChangeEmptyRequest)
                .andExpect(jsonPath("$.empty").value(false));
    }

    @DisplayName("changeNumberOfGuests: 손님 수를 변경하는 테스")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderTable saved = orderTableRepository.save(orderTable);

        final TableChangeGuestsRequest tableChangeGuestsRequest = new TableChangeGuestsRequest(5);

        update("/api/tables/" + saved.getId() + "/number-of-guests", tableChangeGuestsRequest)
                .andExpect(jsonPath("$.numberOfGuests").value(5));
    }
}
