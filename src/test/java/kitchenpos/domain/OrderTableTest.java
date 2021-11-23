package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @Test
    @DisplayName("OrderTable 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        int numberOfGuests = 2;
        boolean empty = true;

        //when
        OrderTable actual = OrderTable.create(numberOfGuests, empty);

        //then
        assertEquals(actual.getNumberOfGuests(), numberOfGuests);
        assertEquals(actual.isEmpty(), empty);
    }

    @Test
    @DisplayName("TableGroup에 group될 때 성공 테스트")
    public void groupTest() throws Exception {
        //given
        OrderTable orderTable = OrderTable.create(2, true);
        TableGroup tableGroup = TableGroup.create(LocalDateTime.now());

        //when
        orderTable.group(tableGroup);

        //then
        assertEquals(orderTable.getTableGroup(), tableGroup);
    }

    @Test
    @DisplayName("TableGroup에 group될 때 실패 테스트 - 테이블이 이미 찼을 때")
    public void groupTest_WithFull() throws Exception {
        //given
        OrderTable orderTable = OrderTable.create(2, false);
        TableGroup tableGroup = TableGroup.create(LocalDateTime.now());

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderTable.group(tableGroup);
        }).withMessage("테이블에 손님이 있거나 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다.");
    }

    @Test
    @DisplayName("TableGroup에 group될 때 실패 테스트 - 테이블 그룹에 이미 속해 있을 때")
    public void groupTest_WithTableGroup() throws Exception {
        //given
        TableGroup tableGroup1 = TableGroup.create(LocalDateTime.now());
        OrderTable orderTable = OrderTable.create(1L, tableGroup1, 2, false);
        TableGroup tableGroup2 = TableGroup.create(LocalDateTime.now());

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderTable.group(tableGroup2);
        }).withMessage("테이블에 손님이 있거나 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다.");
    }

    @Test
    @DisplayName("ungroup 성공 테스트")
    public void ungroupTest() throws Exception {
        //given
        OrderTable orderTable = OrderTable.create(2, true);
        TableGroup tableGroup = TableGroup.create(LocalDateTime.now());
        orderTable.group(tableGroup);

        //when
        orderTable.ungroup();

        //then
        assertNull(orderTable.getTableGroup());
        assertFalse(orderTable.isEmpty());
    }

    @Test
    @DisplayName("changeNumberOfGuests 성공 테스트")
    public void changeNumberOfGuestsTest() throws Exception {
        //given
        OrderTable orderTable = OrderTable.create(2, false);

        //when
        orderTable.changeNumberOfGuests(NumberOfGuests.create(4));

        //then
        assertEquals(orderTable.getNumberOfGuests(), 4);
    }

    @Test
    @DisplayName("changeNumberOfGuests 실패 테스트 - 테이블이 비어있을 때")
    public void changeNumberOfGuestsTest_WithEmptyTable() throws Exception {
        //given
        OrderTable orderTable = OrderTable.create(2, true);

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderTable.changeNumberOfGuests(NumberOfGuests.create(4));
        }).withMessage("테이블이 비어있으면 손님 수 변경이 불가능합니다.");
    }
}
