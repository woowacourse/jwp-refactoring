package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.dto.TableChangeEmptyRequest;
import kitchenpos.dto.TableChangeGuestsRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableResponse;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("create: 테이블 등록 생성 테스트")
    @Test
    void createTest() {
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);

        final TableResponse expected = tableService.create(tableCreateRequest);

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(expected.getNumberOfGuests()).isZero(),
                () -> assertThat(expected.getEmpty()).isTrue()
        );
    }

    @DisplayName("list: 테이블 전체 조회 테스트")
    @Test
    void listTest() {
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        tableService.create(tableCreateRequest);
        tableService.create(tableCreateRequest);

        final List<TableResponse> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables).hasSize(2),
                () -> assertThat(orderTables.get(0).getId()).isNotNull(),
                () -> assertThat(orderTables.get(1).getId()).isNotNull()
        );
    }

    @DisplayName("changeEmpty: 테이블의 비어있는 상태를 변경하는 테스트")
    @Test
    void changeEmptyTest() {
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        final TableResponse tableResponse = tableService.create(tableCreateRequest);
        final TableChangeEmptyRequest tableChangeEmptyRequest = new TableChangeEmptyRequest(false);

        final TableResponse expected = tableService.changeEmpty(tableResponse.getId(), tableChangeEmptyRequest);

        assertThat(expected.getEmpty()).isFalse();
    }

    @DisplayName("changeEmpty: 테이블 주문 상태가 음식을 먹고 있거나, 요리중이면 예외처리")
    @Test
    void changeEmptyTestByOrderStatusEqualsCookingAndMeal() {
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(0, false);
        final TableResponse tableResponse = tableService.create(tableCreateRequest);
        final TableChangeEmptyRequest tableChangeEmptyRequest = new TableChangeEmptyRequest(true);
        final Order order = new Order(tableResponse.getId(), "COOKING", new ArrayList<>());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(tableResponse.getId(), tableChangeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요리중이거나 식사중이면 상태를 변경할 수 없습니다.");
    }

    @DisplayName("changeNumberOfGuests: 손님 수를 변경하는 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(0, false);
        final TableResponse tableResponse = tableService.create(tableCreateRequest);
        final TableChangeGuestsRequest tableChangeGuestsRequest = new TableChangeGuestsRequest(5);
        final TableResponse actual = tableService.changeNumberOfGuests(tableResponse.getId(),
                tableChangeGuestsRequest);

        assertThat(actual.getNumberOfGuests()).isEqualTo(tableChangeGuestsRequest.getNumberOfGuests());
    }

    @DisplayName("changeNumberOfGuests: 손님 수가 0보다 작으면 예외처리")
    @Test
    void changeNumberOfGuestsTestByZero() {
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(0, false);
        final TableResponse tableResponse = tableService.create(tableCreateRequest);
        final TableChangeGuestsRequest tableChangeGuestsRequest = new TableChangeGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableResponse.getId(), tableChangeGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수가 0보다 작을수 없습니다.");
    }

    @DisplayName("changeNumberOfGuests: 테이블이 비어있는데 손님수를 변경하려면 예외치리")
    @Test
    void changeNumberOfGuestsTestByEmpty() {
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        final TableResponse tableResponse = tableService.create(tableCreateRequest);
        final TableChangeGuestsRequest tableChangeGuestsRequest = new TableChangeGuestsRequest(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableResponse.getId(), tableChangeGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블이 비어있는 상태에서는 인원을 변경할 수 없습니다.");
    }
}
