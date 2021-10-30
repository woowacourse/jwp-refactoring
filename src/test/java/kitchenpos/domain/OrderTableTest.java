package kitchenpos.domain;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderFixture.COOKING_ORDER;
import static kitchenpos.fixture.TableGroupFixture.GROUP1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderTable 단위 테스트")
class OrderTableTest {

    @Test
    @DisplayName("테이블의 손님수와 빈(empty) 여부 조건이 있다면 생성한다. 생성 후 테이블은 아무 그룹에도 속해있지 않아야한다.")
    void create() {
        // when & then
        assertDoesNotThrow(() -> {
            OrderTable table = new OrderTable(4, false);
            assertNull(table.getId());
            assertNull(table.getTableGroup());
        });
    }

    @Test
    @DisplayName("테이블의 빈(empty) 여부를 수정할 수 있다.")
    void changeEmpty() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, false);

        // when
        table.changeEmpty(true);

        // then
        assertTrue(table.isEmpty());
    }

    @Test
    @DisplayName("테이블이 특정 그룹에 속해있다면 빈(empty) 여부를 수정할 수 없다.")
    void changeEmptyBelongToGroup() {
        // given
        OrderTable table = new OrderTable(1L, GROUP1, 4, false);

        // when & then
        assertThatThrownBy(() -> table.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 그룹에 속해있습니다. 그룹을 해제해주세요.");
    }

    @Test
    @DisplayName("주문 상태가 조리중(COOKING)이나 식사중(MEAL)이라면, 빈(empty) 여부를 수정할 수 없다.")
    void changeEmptyCookingOrMeal() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, false, Collections.singletonList(COOKING_ORDER));

        // when & then
        assertThatThrownBy(() -> table.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리중이나 식사중입니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수를 수정할 수 있다.")
    void changeNumberOfGuests() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, false);

        // when
        table.changeNumberOfGuests(5);

        // then
        assertEquals(5, table.getNumberOfGuests());
    }

    @Test
    @DisplayName("테이블의 손님 수를 음수로 수정할 수 없다.")
    void changeNumberOfGuestsMinus() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, false);

        // when & then
        assertThatThrownBy(() -> table.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("변경하려는 손님 수는 0이상이어야 합니다.");
    }

    @Test
    @DisplayName("비어있는 테이블의 손님 수를 수정할 수 없다.")
    void changeNumberOfGuestsEmptyTable() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, true);

        // when & then
        assertThatThrownBy(() -> table.changeNumberOfGuests(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있는 테이블의 손님 수를 변경할 수 없습니다.");
    }
}
