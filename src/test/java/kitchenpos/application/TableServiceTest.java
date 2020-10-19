package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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

import static kitchenpos.DomainFactory.createOrderTable;
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
        OrderTable orderTable = createOrderTable(0, true);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests()),
                () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty())
        );
    }

    @DisplayName("저장된 모든 테이블 반환")
    @Test
    void listTest() {
        saveOrderTable(orderTableRepository, 0, true);
        saveOrderTable(orderTableRepository, 0, true);

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("테이블에 주문이 남아있는지 여부 상태 변경")
    @Test
    void changeEmptyTest() {
        OrderTable orderTable = saveOrderTable(orderTableRepository, 0, true);
        OrderTable nonEmptyOrderTable = createOrderTable(0, false);

        OrderTable changeEmptyOrderTable = tableService.changeEmpty(orderTable.getId(), nonEmptyOrderTable);

        assertAll(
                () -> assertThat(changeEmptyOrderTable.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(changeEmptyOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests()),
                () -> assertThat(changeEmptyOrderTable.isEmpty()).isEqualTo(nonEmptyOrderTable.isEmpty())
        );
    }

    @DisplayName("잘못된 테이블 번호 입력 시 예외 반환")
    @Test
    void changeEmptyWithInvalidOrderTableIdTest() {
        OrderTable orderTable = createOrderTable(0, false);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(0L, orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체로 지정된 테이블의 상태 변경 시 예외 반환")
    @Test
    void changeEmptyWithTableGroupTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupRepository);
        OrderTable orderTable = saveOrderTable(orderTableRepository, 1, true, savedTableGroup.getId());
        OrderTable nonEmptyOrderTable = createOrderTable(1, false);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), nonEmptyOrderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 주문이 남아있을 때 상태 변경 시 예외 반환")
    @Test
    void changeEmptyWithOrderTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupRepository);
        OrderTable unPairedOrderTable = saveOrderTable(orderTableRepository, 1, true, savedTableGroup.getId());
        OrderTable nonEmptyOrderTable = createOrderTable(1, false);
        saveOrder(orderRepository, unPairedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());

        assertThatThrownBy(() -> {
            tableService.changeEmpty(unPairedOrderTable.getId(), nonEmptyOrderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuestsTest() {
        OrderTable orderTable = saveOrderTable(orderTableRepository, 5, false);
        OrderTable changeGuestsOrderTable = createOrderTable(3, false);

        OrderTable changeNumberOfGuestsOrderTable =
                tableService.changeNumberOfGuests(orderTable.getId(), changeGuestsOrderTable);

        assertAll(
                () -> assertThat(changeNumberOfGuestsOrderTable.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(changeNumberOfGuestsOrderTable.getNumberOfGuests())
                        .isEqualTo(changeGuestsOrderTable.getNumberOfGuests()),
                () -> assertThat(changeNumberOfGuestsOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty())
        );
    }

    @DisplayName("0명 미만으로 손님 수 변경 시 예외 반환")
    @Test
    void changeNumberOfGuestsWithUnderZeroTest() {
        OrderTable orderTable = saveOrderTable(orderTableRepository, 5, false);
        OrderTable changeGuestsOrderTable = createOrderTable(-1, false);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), changeGuestsOrderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("잘못된 테이블 번호 입력 시 예외 반환")
    @Test
    void changeNumberOfGuestsWithInvalidOrderTableIdTest() {
        OrderTable changeGuestsOrderTable = createOrderTable(3, false);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(0L, changeGuestsOrderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 비어있는 테이블의 손님 수 변경 시 예외 반환")
    @Test
    void changeNumberOfGuestsWithEmptyOrderTableTest() {
        OrderTable emptyOrderTable = saveOrderTable(orderTableRepository, 5, true);
        OrderTable changeGuestsOrderTable = createOrderTable(3, false);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(emptyOrderTable.getId(), changeGuestsOrderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}