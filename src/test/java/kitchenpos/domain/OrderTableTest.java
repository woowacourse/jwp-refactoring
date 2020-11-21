package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableTest {
    @Mock
    private TableOrderEmptyValidator tableOrderEmptyValidator;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        assertThat(new OrderTable(1, true)).isNotNull();
    }

    @DisplayName("테이블 생성 시 손님 수가 음수이면 예외 처리한다.")
    @Test
    void createWithNegativeNumberOfGuests() {
        assertThatThrownBy(() -> new OrderTable(-1, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태를 비도록 수정한다.")
    @Test
    void changeEmpty() {
        doNothing().when(tableOrderEmptyValidator).validate(anyLong());

        OrderTable orderTable = new OrderTable(1L, null, 1, false);
        orderTable.changeEmpty(true, tableOrderEmptyValidator);
    }

    @DisplayName("메뉴 그룹이 있을 경우 테이블 상태를 변경하면 예외 처리한다.")
    @Test
    void changeEmptyWithGroupId() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        assertThatThrownBy(() -> orderTable.changeEmpty(true, tableOrderEmptyValidator))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("테이블의 현재 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeNumberOfGuests(4);
    }

    @DisplayName("테이블의 손님 수를 변경할 시 수정될 값이 음수일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        OrderTable orderTable = new OrderTable(1, false);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-4))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 시 비어있는 테이블일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWithEmpty() {
        OrderTable orderTable = new OrderTable(1, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(4))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("현재 테이블을 그룹 지정한다.")
    @Test
    void designateGroup() {
        OrderTable orderTable = new OrderTable(1, true);

        orderTable.designateGroup(2L);

        assertAll(
            () -> assertThat(orderTable).extracting(OrderTable::getTableGroupId).isEqualTo(2L),
            () -> assertThat(orderTable).extracting(OrderTable::isEmpty, BOOLEAN).isFalse()
        );
    }

    @DisplayName("테이블이 비어있지 않을 때 그룹 지정을 하면 예외 처리한다.")
    @Test
    void designateGroupWithEmpty() {
        OrderTable orderTable = new OrderTable(1, false);

        assertThatThrownBy(() -> orderTable.designateGroup(2L))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("테이블이 이미 그룹 지정이 되어 있으면 예외 처리한다.")
    @Test
    void designateGroupWithGroup() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

        assertThatThrownBy(() -> orderTable.designateGroup(2L))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("테이블의 그룹 지정을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        orderTable.ungroup();

        assertAll(
            () -> assertThat(orderTable).extracting(OrderTable::getTableGroupId).isNull(),
            () -> assertThat(orderTable).extracting(OrderTable::isEmpty, BOOLEAN).isFalse()
        );
    }
}