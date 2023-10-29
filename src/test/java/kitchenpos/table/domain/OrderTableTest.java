package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    private final OrderTableValidator mockValidator = Mockito.mock(OrderTableValidator.class);

    @BeforeEach
    void setUp() {
        doNothing().when(mockValidator).validateCanChangeEmpty(any(OrderTable.class));
    }

    @Test
    @DisplayName("테이블을 그룹화하면 테이블은 비어있지 않은 상태가 된다")
    void groupBy() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable orderTable = new OrderTable(5, true);

        // when
        orderTable.groupBy(tableGroup.getId());

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블을 그룹화할 때 테이블이 비어있지 않으면 예외가 발생한다")
    void groupBy_notEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable orderTable = new OrderTable(5, false);

        // when & then
        assertThatThrownBy(() -> orderTable.groupBy(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 그룹화하려면 테이블이 비어있고 그룹화되어있지 않아야 합니다.");
    }

    @Test
    @DisplayName("테이블을 그룹화할 때 테이블 그룹이 없다면 예외가 발생한다")
    void groupBy_nullTableGroup() {
        // given
        final OrderTable orderTable = new OrderTable(5, false);

        // when & then
        assertThatThrownBy(() -> orderTable.groupBy(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 그룹화하려면 테이블이 비어있고 그룹화되어있지 않아야 합니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블이 비어 있다면 변경할 수 없다")
    void changeNumberOfGuests_emptyTable() {
        // given
        final OrderTable 두명_테이블 = new OrderTable(2, true);

        // when & then
        assertThatThrownBy(() -> 두명_테이블.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 손님 수가 음수이면 안 된다")
    void changeNumberOfGuests_negativeNumberOfGuests() {
        // given
        final OrderTable 두명_테이블 = new OrderTable(2, false);
        final int invalidNumberOfGuests = -1;

        // when & then
        assertThatThrownBy(() -> 두명_테이블.changeNumberOfGuests(invalidNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
    }
}
