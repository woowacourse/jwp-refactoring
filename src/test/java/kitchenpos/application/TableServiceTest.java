package kitchenpos.application;

import static kitchenpos.Fixture.DomainFixture.GUEST_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableEmptyChangeRequest;
import kitchenpos.dto.request.OrderTableGuestNumberChangeRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.GuestNumberChangeDisabledException;
import kitchenpos.exception.InvalidGuestNumberException;
import kitchenpos.exception.TableEmptyDisabledException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("Table을 생성할 수 있다.")
    @Test
    void create() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(3, false);

        tableService.create(orderTableCreateRequest);

        assertThat(tableService.list()).hasSize(1);
    }

    @DisplayName("0보다 작은 수로 Table을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_InvalidGuestNumber() {
        int invalidGuestNumber = 0;
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(invalidGuestNumber, false);

        assertThatThrownBy(() -> tableService.create(orderTableCreateRequest))
                .isInstanceOf(InvalidGuestNumberException.class);
    }

    @DisplayName("Table의 empty 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateRequest(3, false));

        tableService.changeEmpty(orderTableResponse.getId(), new OrderTableEmptyChangeRequest(true));

        OrderTable changedOrderTable = tableRepository.findAll()
                .stream()
                .filter(OrderTable::isEmpty)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("TableGroup에 속해있는 Table의 empty 상태를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeEmpty_Exception_GroupedTable() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, true, tableGroup));

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyChangeRequest(true)))
                .isInstanceOf(TableEmptyDisabledException.class)
                .hasMessage("Table Group으로 묶인 테이블은 empty를 변경할 수 없습니다.");
    }

    @DisplayName("COOKING, MEAL 상태인 Table의 상태를 변경하려고 하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COOKING"})
    void changeEmpty_Exception_NotCompleteOrderStatus(String orderStatus) {
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, false));
        Order order = Order.newOrder(orderTable);
        order.changeOrderStatus(OrderStatus.from(orderStatus));
        orderRepository.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyChangeRequest(true)))
                .isInstanceOf(TableEmptyDisabledException.class)
                .hasMessage("조리중이거나 식사중인 테이블의 empty를 변경할 수 없습니다.");
    }

    @DisplayName("Table의 numberOfGuests 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, false));

        tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableGuestNumberChangeRequest(200));

        Optional<OrderTable> changedOrderTable = tableRepository.findAll()
                .stream()
                .filter(orderTable1 -> orderTable1.getGuestNumber() == 200)
                .findAny();
        assertThat(changedOrderTable).isNotEmpty();
    }

    @DisplayName("0보다 작은 numberOfGuests로 Table을 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeNumberOfGuests_Exception_InvalidNumberOfGuests() {
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, false));
        int invalidNumberOfGuests = -1;

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(
                        orderTable.getId(), new OrderTableGuestNumberChangeRequest(invalidNumberOfGuests)))
                .isInstanceOf(InvalidGuestNumberException.class);
    }

    @DisplayName("empty 상태인 Table의 numberOfGuests 수를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeNumberOfGuests_Exception_EmptyOrderTable() {
        OrderTable orderTable = tableRepository.save(new OrderTable(GUEST_NUMBER, true));

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(),
                        new OrderTableGuestNumberChangeRequest(10)))
                .isInstanceOf(GuestNumberChangeDisabledException.class);
    }
}
