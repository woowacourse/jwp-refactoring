package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
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

    private Long notEmptyOrderTableId;

    @BeforeEach
    void setup(){
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 0, false));
        notEmptyOrderTableId = orderTable.getId();
    }

    @DisplayName("주문테이블을 등록할 수 있다.")
    @Test
    void create() {
        final OrderTable orderTable = new OrderTable(null, 0, true);

        final OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("손님 수를 변경하는 기능")
    @Nested
    class changeNumberOfGuestsTest {

        @DisplayName("정상적으로 변경한다.")
        @Test
        void changeNumberOfGuests() {
            final OrderTable orderTable = new OrderTable(null, 1, true);

            final OrderTable savedOrderTable = tableService.changeNumberOfGuests(notEmptyOrderTableId, orderTable);

            assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(1);
        }

        @DisplayName("변경하려는 손님의 수가 0 미만이면 예외가 발생한다.")
        @Test
        void changeNumberOfGuests_Exception_NumOfGuests() {
            final OrderTable orderTable = new OrderTable(null, -1, true);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notEmptyOrderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("기존에 저장된 테이블이 emtpy 상태면 예외가 발생한다.")
        @Test
        void changeNumberOfGuests_Exception_Empty() {
            final OrderTable orderTable = new OrderTable(null, 1, true);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문테이블의 empty 상태를 변경하는 기능")
    @Nested
    class changeEmptyTest {

        @DisplayName("정상 변경")
        @Test
        void changeEmpty() {
            final OrderTable orderTable = new OrderTable(null, 0, true);

            final OrderTable modifiedOrderTable = tableService.changeEmpty(notEmptyOrderTableId, orderTable);

            assertThat(modifiedOrderTable.isEmpty()).isTrue();
        }

        @DisplayName("테이블 그룹이 존재하면 예외가 발생한다.")
        @Test
        void changeEmpty_Exception() {
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), Collections.emptyList()));
            final OrderTable groupedOrderTable = orderTableDao.save(new OrderTable(1L, 0, false));
            final OrderTable orderTable = new OrderTable(1L, 1, true);

            assertThatThrownBy(() -> tableService.changeEmpty(groupedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문테이블에서 일어난 주문들 중 상태가 조리, 식사인 주문이 존재할 경우 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void changeEmpty_Exception_OrderStatus(OrderStatus orderStatus) {
            orderDao.save(new Order(notEmptyOrderTableId, orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));
            final OrderTable orderTable = new OrderTable(null, 1, true);

            assertThatThrownBy(() -> tableService.changeEmpty(notEmptyOrderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
