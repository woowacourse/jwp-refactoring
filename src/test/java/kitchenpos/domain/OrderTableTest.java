package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class OrderTableTest {

    @DisplayName("정상적인 경우 테이블 상태를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeTableStatus(final boolean empty) {
        final OrderTable sut = new OrderTable(5, empty);
        final boolean changedTableStatus = !empty;

        sut.changeEmpty(changedTableStatus);

        assertThat(sut.isEmpty()).isEqualTo(changedTableStatus);
    }

    @DisplayName("이미 단체로 지정된 테이블은 테이블 상대를 변경할 수 없다.")
    @Test
    void changeAlreadyGroupTableStatus() {
        final OrderTable sut = new OrderTable(5, true);
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        sut.groupTable(tableGroup.getId());

        assertThatThrownBy(() -> sut.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적인 경우 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable sut = new OrderTable(5, false);

        sut.changeNumberOfGuests(10);

        assertThat(sut.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("손님 수를 0명 미만으로 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsLessThanZero() {
        final OrderTable sut = new OrderTable(5, false);

        assertThatThrownBy(() -> sut.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이면 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        final OrderTable sut = new OrderTable(5, true);

        assertThatThrownBy(() -> sut.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
