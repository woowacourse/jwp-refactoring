package kitchenpos.application.order;

import static kitchenpos.support.fixture.OrderTableFixture.createEmptyStatusTable;
import static kitchenpos.support.fixture.OrderTableFixture.createNonEmptyStatusTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

    private OrderTable nonEmptyOrderTable;

    @BeforeEach
    void setupFixture() {
        nonEmptyOrderTable = orderTableDao.save(createNonEmptyStatusTable());
    }

    @DisplayName("주문테이블을 등록할 수 있다.")
    @Test
    void create() {
        final OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);

        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        final Optional<OrderTable> savedOrderTable = orderTableDao.findById(orderTableResponse.getId());
        assertThat(savedOrderTable).isPresent();
    }

    @DisplayName("손님 수를 변경하는 기능")
    @Nested
    class changeNumberOfGuestsTest {

        @DisplayName("정상적으로 변경한다.")
        @Test
        void changeNumberOfGuests() {
            final OrderTableChangeGuestsRequest orderTableChangeGuestsRequest = new OrderTableChangeGuestsRequest(100);

            tableService.changeNumberOfGuests(nonEmptyOrderTable.getId(), orderTableChangeGuestsRequest);

            final OrderTable orderTable = orderTableDao.findById(nonEmptyOrderTable.getId()).orElseThrow();
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(orderTableChangeGuestsRequest.getNumberOfGuests());
        }

        @DisplayName("변경하려는 손님의 수가 0 미만이면 예외가 발생한다.")
        @Test
        void changeNumberOfGuests_Exception_NumOfGuests() {
            final OrderTableChangeGuestsRequest orderTableChangeGuestsRequest = new OrderTableChangeGuestsRequest(-1);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(nonEmptyOrderTable.getId(), orderTableChangeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("기존에 저장된 테이블이 emtpy 상태면 예외가 발생한다.")
        @Test
        void changeNumberOfGuests_Exception_Empty() {
            final OrderTable emptyTable = orderTableDao.save(createEmptyStatusTable());
            final OrderTableChangeGuestsRequest orderTableChangeGuestsRequest = new OrderTableChangeGuestsRequest(100);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), orderTableChangeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문테이블의 empty 상태를 변경하는 기능")
    @Nested
    class changeEmptyTest {

        @DisplayName("정상 변경")
        @Test
        void changeEmpty() {
            final OrderTableChangeStatusRequest orderTableChangeStatusRequest = new OrderTableChangeStatusRequest(true);

            tableService.changeEmpty(nonEmptyOrderTable.getId(), orderTableChangeStatusRequest);

            final OrderTable orderTable = orderTableDao.findById(nonEmptyOrderTable.getId()).orElseThrow();
            assertThat(orderTable.isEmpty()).isTrue();
        }

        @DisplayName("테이블 그룹이 존재하면 예외가 발생한다.")
        @Test
        void changeEmpty_Exception() {
            final TableGroup savedTableGroup = tableGroupDao
                    .save(new TableGroup(LocalDateTime.now(), Collections.emptyList()));
            final OrderTable groupedOrderTable = orderTableDao.save(new OrderTable(savedTableGroup, 0, false));
            final OrderTableChangeStatusRequest orderTableChangeStatusRequest = new OrderTableChangeStatusRequest(true);

            assertThatThrownBy(() -> tableService.changeEmpty(groupedOrderTable.getId(), orderTableChangeStatusRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문테이블에서 일어난 주문들 중 상태가 조리, 식사인 주문이 존재할 경우 예외가 발생한다.")
        @ParameterizedTest(name = "주문테이블에서 일어난 주문들 중 상태가 {0} 인 주문이 존재할 경우 예외가 발생한다.")
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void changeEmpty_Exception_OrderStatus(OrderStatus orderStatus) {
            orderDao.save(
                    new Order(nonEmptyOrderTable.getId(), orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));
            final OrderTableChangeStatusRequest orderTableChangeStatusRequest = new OrderTableChangeStatusRequest(true);

            assertThatThrownBy(() -> tableService.changeEmpty(nonEmptyOrderTable.getId(), orderTableChangeStatusRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
