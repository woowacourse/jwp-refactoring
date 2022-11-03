package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.exception.AlreadyGroupedException;
import kitchenpos.exception.CanNotGroupException;
import kitchenpos.exception.NumberOfGuestsSizeException;
import kitchenpos.exception.TableEmptyException;
import kitchenpos.support.fixtures.OrderTableFixtures;
import kitchenpos.support.fixtures.TableGroupFixtures;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블이 비어있으면 주문이 불가능하다")
    void validateOrderable() {
        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);

        assertThatThrownBy(orderTable::validateOrderable)
                .isExactlyInstanceOf(TableEmptyException.class);
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체가 지정되어 있으면 단체로 지정하지 못한다")
    void validateGroupableWithAlreadyGrouping() {
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 3);
        final TableGroup tableGroup = TableGroupFixtures.create();

        assertThatThrownBy(() -> orderTable.groupTableBy(tableGroup.getId()))
                .isExactlyInstanceOf(CanNotGroupException.class);
    }

    @Test
    @DisplayName("주문 테이블에 이미 손님이 있다면 단체로 지정하지 못한다")
    void validateGroupableWithEmptyOrderTable() {
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 3);
        final TableGroup tableGroup = TableGroupFixtures.create();

        assertThatThrownBy(() -> orderTable.groupTableBy(tableGroup.getId()))
                .isExactlyInstanceOf(CanNotGroupException.class);
    }

    @Test
    @DisplayName("주문 테이블이 단체로 지정되어 있다면 테이블을 비울 수 없다")
    void validateNotGrouping() {
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(tableGroup.getId(), 3);

        assertThatThrownBy(() -> orderTable.updateEmpty(true))
                .isExactlyInstanceOf(AlreadyGroupedException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수는 0보다 작을 수 없다")
    void validateNumberOfGuests() {
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);

        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-1))
                .isExactlyInstanceOf(NumberOfGuestsSizeException.class);
    }
}
