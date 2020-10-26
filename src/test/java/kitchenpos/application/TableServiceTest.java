package kitchenpos.application;

import kitchenpos.application.common.TestFixtureFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.TableGroupRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/delete_all.sql")
class TableServiceTest extends TestFixtureFactory {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("테이블 생성 메서드 테스트")
    @Test
    void create() {
        OrderTableRequest orderTableRequest =
                new OrderTableRequest(0, true);

        OrderTableDto savedOrderTable = tableService.create(orderTableRequest);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("테이블 목록 조회 기능 테스트")
    @Test
    void list() {
        makeSavedOrderTable(0, true);
        makeSavedOrderTable(0, true);

        List<OrderTableDto> tables = tableService.list();

        assertThat(tables).hasSize(2);
    }

    @DisplayName("테이블의 empty 상태를 변경하는 기능 테스트")
    @Test
    void changeEmpty() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        OrderTableDto changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), orderTableRequest);

        assertAll(
                () -> assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(changedOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("테이블의 empty 상태 변경 - 존재하지 않는 아이디를 입력받은 경우 예외 처리")
    @Test
    void changeEmptyWithNotFoundOrderTable() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId() + 100, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 empty 상태 변경 - 단체 테이블에 등록되어 있는 경우 예외 처리")
    @Test
    void changeEmptyWithRegisteredGroupTable() {
        OrderTable savedOrderTable1 = makeSavedOrderTable(0, true);
        OrderTable savedOrderTable2 = makeSavedOrderTable(0, true);

        TableGroupRequest groupingRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableDto(savedOrderTable1.getId()), new OrderTableDto(savedOrderTable2.getId()))
        );
        tableGroupService.create(groupingRequest);

        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 empty 상태 변경 - 주문 상태가 COOKING 혹은 MEAL 인 경우 예외 처리")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void changeEmptyWhenCooking(OrderStatus orderStatus) {
        OrderTable savedOrderTable = makeSavedOrderTable(0, true);

        Order order = Order.of(savedOrderTable, orderStatus);
        orderDao.save(order);

        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님 수를 변경하는 메서드 테스트")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        OrderTableDto changedOrderTableDto =
                tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest);

        assertAll(
                () -> assertThat(changedOrderTableDto.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(changedOrderTableDto.getNumberOfGuests()).isEqualTo(4)
        );
    }

    @DisplayName("테이블에 방문한 손님 수를 변경 - 빈 테이블인 경우 예외 처리")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님 수를 변경 - 입력하려는 숫자가 0보다 작은 경우 예외 처리")
    @Test
    void changeNumberOfGuestsWithLessZeroGuests() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(-1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderDao.deleteAll();
        orderTableDao.deleteAll();
    }
}
