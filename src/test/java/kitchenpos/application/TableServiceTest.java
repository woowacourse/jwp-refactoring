package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {
    @Test
    @DisplayName("테이블을 생성한다")
    void create() {
        final OrderTable actual = saveAndGetOrderTable(1L, true);

        assertThat(actual.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("테이블 전체를 조회한다")
    void list() {
        saveAndGetOrderTable(1L, true);
        saveAndGetOrderTable(2L, false);

        final List<OrderTable> actual = tableService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual)
                        .extracting("id")
                        .containsExactly(1L, 2L)
        );
    }

    @Test
    @DisplayName("테이블을 비운다")
    void changeEmpty() {
        saveAndGetOrder(1L, OrderStatus.COMPLETION.name());

        final OrderTable actual = tableService.changeEmpty(1L, true);

        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("`계산 완료` 상태가 아니라면 테이블을 비울 수 없다")
    void changeEmpty_notCompletionException() {
        saveAndGetOrder(1L, OrderStatus.COOKING.name());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹에 포함된 테이블이라면 테이블을 비울 수 없다")
    void changeEmpty_existGroupException() {
        final OrderTable orderTable = saveAndGetNotEmptyOrderTableInGroup(1L, 1L);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님 수를 변경한다")
    void changeNumberOfGuests() {
        final OrderTable orderTable = saveAndGetOrderTable(1L, false);
        final int expected = 6;

        final OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), expected);

        assertThat(actual.getNumberOfGuests()).isEqualTo(expected);
    }

    @Test
    @DisplayName("손님 수를 0 미만으로 변경하면 예외를 반환한다")
    void changeNumberOfGuests_negativeNumberException() {
        final OrderTable orderTable = saveAndGetOrderTable(1L, false);
        final int expected = -1;

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), expected))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
