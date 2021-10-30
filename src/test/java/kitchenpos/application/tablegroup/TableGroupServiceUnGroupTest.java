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
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(standardOrderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
