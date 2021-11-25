package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("OrderTable 단위 테스트")
class OrderTableTest {

    @DisplayName("empty가 false인 경우, TableGroup 변경이 불가능하다.")
    @Test
    void toTableGroup_Empty_Exception() {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        TableGroup tableGroup = new TableGroup();

        // when, then
        assertThatCode(() -> orderTable.toTableGroup(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OrderTable이 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.");
    }

    @DisplayName("특정 TableGroup에 소속된 경우, TableGroup 변경이 불가능하다.")
    @Test
    void toTableGroup_TableGroupNonNull_Exception() {
        // given
        OrderTable orderTable = new OrderTable(new TableGroup(), 10, false);
        TableGroup tableGroup = new TableGroup();

        // when, then
        assertThatCode(() -> orderTable.toTableGroup(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OrderTable이 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.");
    }

    @DisplayName("empty가 true이고 TableGroup에 소속되지 않은 경우, TableGroup 변경이 가능하다.")
    @Test
    void toTableGroup_Valid_Success() {
        // given
        OrderTable orderTable = new OrderTable( 10, true);
        TableGroup tableGroup = new TableGroup();

        // when
        orderTable.toTableGroup(tableGroup);

        // then
        assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("변경하려는 손님 수가 0 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-2, -1})
    void changeNumberOfGuests_Negative_Exception(int numberOfGuests) {
        // given
        OrderTable orderTable = new OrderTable(10, false);

        // when, then
        assertThatCode(() -> orderTable.changeNumberOfGuests(numberOfGuests))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("변경하려는 손님 수는 음수일 수 없습니다.");
    }

    @DisplayName("현재 OrderTabled의 empty가 true면 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_EmptyTrue_Exception() {
        // given
        OrderTable orderTable = new OrderTable(10, true);

        // when, then
        assertThatCode(() -> orderTable.changeNumberOfGuests(15))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OrderTable이 비어있는 상태입니다.");
    }

    @DisplayName("손님 수가 정상이고 OrderTable empty가 false면 손님 수 변경이 가능하다.")
    @Test
    void changeNumberOfGuests_Valid_Success() {
        // given
        OrderTable orderTable = new OrderTable(10, false);

        // when
        orderTable.changeNumberOfGuests(15);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(15);
    }

    @DisplayName("특정 TableGroup에 소속된 경우 Empty 변경이 불가능하다.")
    @Test
    void changeEmpty_TableGroupExisting_Exception() {
        // given
        OrderTable orderTable = new OrderTable(new TableGroup(), 10, false);

        // when, then
        assertThatCode(() -> orderTable.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OrderTable이 속한 TableGroup이 존재합니다.");
    }

    @DisplayName("Order 중 1개라도 조리 혹은 식사 중이라면 Empty 변경이 불가능하다.")
    @EnumSource(names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void changeEmpty_MealOrCooking_Exception(OrderStatus orderStatus) {
        // given
        List<Order> orders = Arrays.asList(new Order(new OrderTable(10, false), orderStatus));
        OrderTable orderTable = new OrderTable(1L, null, 10, false, orders);

        // when, then
        assertThatCode(() -> orderTable.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.");
    }

    @DisplayName("그 외 정상적인 경우 Empty 변경이 가능하다.")
    @Test
    void changeEmpty_Valid_Success() {
        // given
        List<Order> orders = Arrays.asList(new Order(new OrderTable(10, false), OrderStatus.COMPLETION));
        OrderTable orderTable = new OrderTable(1L, null, 10, false, orders);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("Order 중 1개라도 조리 혹은 식사 중이라면 그룹 해제가 불가능하다.")
    @EnumSource(names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void ungroup_MealOrCooking_Exception(OrderStatus orderStatus) {
        // given
        List<Order> orders = Arrays.asList(new Order(new OrderTable(10, false), orderStatus));
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 10, false, orders);

        // when, then
        assertThatCode(orderTable::ungroup)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.");
    }

    @DisplayName("그 외 정상적인 경우 그룹 해제가 가능하다.")
    @Test
    void ungroup_Valid_Success() {
        // given
        List<Order> orders = Arrays.asList(new Order(new OrderTable(10, false), OrderStatus.COMPLETION));
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 10, false, orders);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }
}
