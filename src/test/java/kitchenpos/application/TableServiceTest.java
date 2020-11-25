package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableResponseDto;

class TableServiceTest extends ServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTableResponseDto orderTableResponse = tableService.create();

        assertThat(orderTableResponse.getId()).isNotNull();
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void list() {
        TableGroup tableGroup = tableGroupDao.save(
            createTableGroup(null, LocalDateTime.now(), new ArrayList<>()));
        orderTableDao.save(createOrderTable(null, tableGroup.getId(), 2, false));

        List<OrderTableResponseDto> orderTables = tableService.list();

        assertThat(orderTables).hasSize(1);
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean status) {
        OrderTableResponseDto orderTableResponse = tableService.create();
        OrderTable emptyTableRequest = createOrderTable(orderTableResponse.getId(),
            orderTableResponse.getTableGroupId(), orderTableResponse.getNumberOfGuests(), status);

        OrderTableResponseDto changedOrderTable = tableService.changeEmpty(emptyTableRequest.getId(),
            emptyTableRequest);

        assertThat(changedOrderTable.isEmpty()).isEqualTo(status);
    }

    @DisplayName("빈 테이블로 변경 시, 주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블로 설정할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_WithInvalidOrderState_ThrownException(String status) {
        OrderTable table = orderTableDao.save(createOrderTable(null, null, 0, false));
        orderDao.save(
            createOrder(null, table.getId(), status, LocalDateTime.now(), new ArrayList<>()));
        OrderTable emptyTableRequest = createOrderTable(table.getId(),
            table.getTableGroupId(), table.getNumberOfGuests(), true);

        assertThatThrownBy(() -> tableService.changeEmpty(emptyTableRequest.getId(),
            emptyTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(
            createOrderTable(null, null, 1, false));

        OrderTableResponseDto changedOrderTable = tableService.changeNumberOfGuests(
            orderTable.getId(), 3);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }
}