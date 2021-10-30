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

    @DisplayName("테이블을 빈 상태로 변경할 시에 테이블이 존재해야만 한다.")
    @Test
    void unexsistedTable() {
        //given
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 저장된 테이블 그룹에는 없어야 한다.")
    @Test
    void saveOrderTable() {
        //given
        standardTable.setTableGroupId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요리 중이거나 식사중인 상태의 주문이 없어야 한다.")
    @Test
    void cookAndMealStatus() {
        //given
        standardTable.setTableGroupId(null);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 빈 상태를 변경한다.")
    @Test
    void changeTableEmptyStatus() {
        //given
        standardTable.setEmpty(false);
        standardTable.setTableGroupId(null);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.changeEmpty(1L, standardTable);

        //then
        assertThat(orderTable.isEmpty()).isFalse();
    }

}
