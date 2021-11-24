package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTablesTest {

    @Test
    @DisplayName("OrderTables 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        OrderTable orderTable1 = OrderTable.create(1, true);
        OrderTable orderTable2 = OrderTable.create(2, true);
        OrderTable orderTable3 = OrderTable.create(3, true);

        //when
        OrderTables actual = OrderTables
                .create(Arrays.asList(orderTable1, orderTable2, orderTable3));

        //then
        assertEquals(actual.getSize(), 3);
    }

    @Test
    @DisplayName("OrderTables 생성 시 size가 2 미만일 때 예외 테스트")
    public void validateNotEnoughSizeTest() throws Exception {
        //given
        OrderTable orderTable1 = OrderTable.create(1, true);

        //when

        //then
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> {
            OrderTables.create(Collections.singletonList(orderTable1));
        }).withMessage("orderTables는 2개 이상의 size여야 합니다.");
    }
}
