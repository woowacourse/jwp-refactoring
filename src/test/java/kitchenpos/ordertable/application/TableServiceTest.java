package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.ServiceTest;
import kitchenpos.order.model.Order;
import kitchenpos.order.model.OrderStatus;
import kitchenpos.ordertable.model.OrderTable;
import kitchenpos.tablegroup.model.TableGroup;
import kitchenpos.ordertable.application.dto.OrderTableResponseDto;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;

class TableServiceTest extends ServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTableResponseDto orderTableResponse = tableService.create();

        assertThat(orderTableResponse.getId()).isNotNull();
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void list() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTableRepository.save(new OrderTable(null, tableGroup.getId(), 2, false));

        List<OrderTableResponseDto> orderTables = tableService.list();

        assertThat(orderTables).hasSize(1);
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean status) {
        OrderTableResponseDto orderTableResponse = tableService.create();

        OrderTableResponseDto changedOrderTable = tableService.changeEmpty(orderTableResponse.getId(), status);

        assertThat(changedOrderTable.isEmpty()).isEqualTo(status);
    }

    @DisplayName("빈 테이블로 변경 시, 주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블로 설정할 수 없다.")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void changeEmpty_WithInvalidOrderState_ThrownException(OrderStatus status) {
        OrderTable table = orderTableRepository.save(new OrderTable(null, null, 0, false));
        orderRepository.save(new Order(null, table.getId(), status, LocalDateTime.now()));

        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, null, 1, false));

        OrderTableResponseDto changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), 3);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }
}