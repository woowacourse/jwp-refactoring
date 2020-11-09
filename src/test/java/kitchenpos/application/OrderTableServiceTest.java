package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.application.dto.OrderTableResponse;

@Transactional
@SpringBootTest
class OrderTableServiceTest {
    @Autowired
    private OrderTableService orderTableService;

    @DisplayName("테이블 생성")
    @Test
    void create() {
        OrderTableResponse response = orderTableService.create(ORDER_TABLE_REQUEST);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        orderTableService.create(ORDER_TABLE_REQUEST);
        List<OrderTableResponse> list = orderTableService.list();

        assertThat(list.isEmpty()).isFalse();
    }

    @DisplayName("테이블 손님 여부 변경")
    @Test
    void changeEmpty() {
        OrderTableResponse response = orderTableService.create(ORDER_TABLE_REQUEST);
        boolean empty = !ORDER_TABLE_CHANGE_EMPTY_REQUEST.isEmpty();

        orderTableService.changeEmpty(response.getId(), new OrderTableChangeEmptyRequest(empty));
        List<OrderTableResponse> list = orderTableService.list();
        OrderTableResponse last = list.get(list.size() - 1);

        assertThat(last.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("테이블 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        Long tableId = orderTableService.create(ORDER_TABLE_REQUEST).getId();
        int numberOfGuests = ORDER_TABLE_CHANGE_NUMBER_OF_GUESTS.getNumberOfGuests();

        orderTableService.changeEmpty(tableId, new OrderTableChangeEmptyRequest(false));
        orderTableService.changeNumberOfGuests(tableId, new OrderTableChangeNumberOfGuests(numberOfGuests));
        List<OrderTableResponse> list = orderTableService.list();
        OrderTableResponse last = list.get(list.size() - 1);

        assertThat(last.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}