package kitchenpos.application.tablegroup;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupServiceUnGroupTest extends TableGroupServiceTest {

    @DisplayName("단체를 삭제시, 요리하거나 식사중인 테이블이 없어야만 한다.")
    @Test
    void withCookOrMeal() {
        //given
        given(orderTableDao.findAllByTableGroupId(BASIC_TABLE_GROUP_ID)).willReturn(
            standardOrderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(FIRST_TABLE_ID, SECOND_TABLE_ID),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(BASIC_TABLE_GROUP_ID))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
