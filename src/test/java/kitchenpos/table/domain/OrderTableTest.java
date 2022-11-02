package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("손님 수를 변경하는 기능")
    @Nested
    class changeNumberOfGuestsTest {

        @DisplayName("변경하려는 손님의 수가 0 미만이면 예외가 발생한다.")
        @Test
        void changeNumberOfGuests_Exception_NumOfGuests() {
            final OrderTable orderTable = OrderTable.createWithoutTableGroup(0, false);
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("기존 테이블이 emtpy 상태면 예외가 발생한다.")
        @Test
        void changeNumberOfGuests_Exception_Empty() {
            final OrderTable orderTable = OrderTable.createWithoutTableGroup(0, true);

            assertThatThrownBy(
                    () -> orderTable.changeNumberOfGuests(100))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문테이블의 empty 상태를 변경하는 기능")
    @Nested
    class changeEmptyTest {

        @DisplayName("테이블 그룹이 존재하면 예외가 발생한다.")
        @Test
        void changeEmpty_Exception() {
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                    List.of(new OrderTable(null, 0, true),
                            new OrderTable(null, 0, true)));
            final OrderTable orderTable = new OrderTable(tableGroup, 0, true);

            assertThatThrownBy(() -> orderTable.changeEmptyStatus(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
