package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableServiceTest extends ServiceTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @DisplayName("새로운 테이블 추가")
    @Test
    void createTest() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);

        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        assertAll(
                () -> assertThat(orderTableResponse.getId()).isNotNull(),
                () -> assertThat(orderTableResponse.getTableGroupId()).isNull(),
                () -> assertThat(orderTableResponse.getNumberOfGuests())
                        .isEqualTo(orderTableRequest.getNumberOfGuests()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
        );
    }

    @DisplayName("저장된 모든 테이블 반환")
    @Test
    void listTest() {
        saveOrderTable(orderTableRepository, 0, true);
        saveOrderTable(orderTableRepository, 0, true);

        List<OrderTableResponse> orderTableResponses = tableService.list();

        assertThat(orderTableResponses).hasSize(2);
    }

    @DisplayName("테이블에 주문이 남아있는지 여부 상태 변경")
    @Test
    void changeEmptyTest() {
        OrderTable orderTable = saveOrderTable(orderTableRepository, 0, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        OrderTableResponse orderTableResponse =
                tableService.changeEmpty(orderTable.getId(), orderTableRequest);

        assertAll(
                () -> assertThat(orderTableResponse.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuestsCount()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
        );
    }

    @DisplayName("잘못된 테이블 번호 입력 시 예외 반환")
    @Test
    void changeEmptyWithInvalidOrderTableIdTest() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(0L, orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체로 지정된 테이블의 상태 변경 시 예외 반환")
    @Test
    void changeEmptyWithTableGroupTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupRepository);
        OrderTable orderTable = saveOrderTable(orderTableRepository, 1, true, savedTableGroup.getId());
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 주문이 남아있을 때 상태 변경 시 예외 반환")
    @Test
    void changeEmptyWithOrderTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupRepository);
        OrderTable unPairedOrderTable = saveOrderTable(orderTableRepository, 1, true, savedTableGroup.getId());
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);
        saveOrder(orderRepository, unPairedOrderTable.getId(), OrderStatus.MEAL, LocalDateTime.now());

        assertThatThrownBy(() -> {
            tableService.changeEmpty(unPairedOrderTable.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuestsTest() {
        OrderTable orderTable = saveOrderTable(orderTableRepository, 5, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(3);

        OrderTableResponse orderTableResponse =
                tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);

        assertAll(
                () -> assertThat(orderTableResponse.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(orderTableResponse.getTableGroupId()).isNull(),
                () -> assertThat(orderTableResponse.getNumberOfGuests())
                        .isEqualTo(orderTableRequest.getNumberOfGuests()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTable.isEmptyTable())
        );
    }

    @DisplayName("0명 미만으로 손님 수 변경 시 예외 반환")
    @Test
    void changeNumberOfGuestsWithUnderZeroTest() {
        OrderTable orderTable = saveOrderTable(orderTableRepository, 5, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(-1);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("잘못된 테이블 번호 입력 시 예외 반환")
    @Test
    void changeNumberOfGuestsWithInvalidOrderTableIdTest() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(5);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(0L, orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 비어있는 테이블의 손님 수 변경 시 예외 반환")
    @Test
    void changeNumberOfGuestsWithEmptyOrderTableTest() {
        OrderTable emptyOrderTable = saveOrderTable(orderTableRepository, 5, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(3);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(emptyOrderTable.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}