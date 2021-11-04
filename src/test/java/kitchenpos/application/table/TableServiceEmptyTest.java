package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceEmptyTest extends TableServiceTest {

    private static final Boolean CHANGED_EMPTY_STATE = false;
    private static final Long NULL_TABLE_ID = null;

    @DisplayName("테이블을 빈 상태로 변경할 시에 테이블이 존재해야만 한다.")
    @Test
    void unexsistedTable() {
        //given
        given(orderTableDao.findById(BASIC_ORDER_TABLE_ID)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(BASIC_ORDER_TABLE_ID, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 저장된 테이블 그룹에는 없어야 한다.")
    @Test
    void saveOrderTable() {
        //given
        standardTable.setTableGroupId(BASIC_TABLE_GROUP_ID);
        given(orderTableDao.findById(BASIC_ORDER_TABLE_ID)).willReturn(
            Optional.ofNullable(standardTable));

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(BASIC_ORDER_TABLE_ID, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요리 중이거나 식사중인 상태의 주문이 없어야 한다.")
    @Test
    void cookAndMealStatus() {
        //given
        standardTable.setTableGroupId(NULL_TABLE_ID);
        given(orderTableDao.findById(BASIC_ORDER_TABLE_ID)).willReturn(
            Optional.ofNullable(standardTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(BASIC_ORDER_TABLE_ID,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(
            BASIC_EMPTY_STATE);

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(BASIC_ORDER_TABLE_ID, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 빈 상태를 변경한다.")
    @Test
    void changeTableEmptyStatus() {
        //given
        standardTable.setEmpty(CHANGED_EMPTY_STATE);
        standardTable.setTableGroupId(NULL_TABLE_ID);
        given(orderTableDao.findById(BASIC_ORDER_TABLE_ID)).willReturn(
            Optional.ofNullable(standardTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(BASIC_ORDER_TABLE_ID,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(
            CHANGED_EMPTY_STATE);
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.changeEmpty(BASIC_ORDER_TABLE_ID, standardTable);

        //then
        assertThat(orderTable.isEmpty()).isFalse();
    }

}
