package kitchenpos.domain;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    void 테이블의_인원이_음수일_경우_예외가_발생한다() {
        //given
        int numberOfGuests = -1;
        boolean empty = true;

        //expect
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_인원을_음수로_설정하면_예외가_발생한다() {
        //given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_empty일때_인원수를_변경할_수_없다() {
        //given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Nested
    class empty_설정 {

        @Test
        void 포함된_그룹이_있는_경우_예외가_발생한다() {
            //given
            OrderTable 테이블 = new OrderTable(1, false);
            테이블.changeTableGroup(1L);

            //expect
            assertThatThrownBy(() -> 테이블.changeEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 그룹을_없앨_수_있다() {
        //given
        OrderTable 테이블 = new OrderTable(1, true);
        테이블.changeTableGroup(1L);

        //when
        테이블.ungroup();

        //then
        assertAll(
                () -> assertThat(테이블.isEmpty()).isFalse(),
                () -> assertThat(테이블.getTableGroupId()).isNull()
        );
    }

    @Test
    void id가_같으면_동등하다() {
        //given
        OrderTable 테이블 = new OrderTable(1L, 1L, 1, true);

        //when
        boolean actual = 테이블.equals(new OrderTable(1L, 1L, 1, true));

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 주문할_수_있다() {
        //given
        OrderTable orderTable = new OrderTable(2, false);
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, 1L, 1L));

        //when
        Order order = orderTable.order(orderLineItems);

        //then
        assertThat(order.getOrderLineItems()).containsAll(orderLineItems)
                .usingRecursiveComparison()
                .ignoringFields("id", "order");
    }

    @Test
    void 테이블이_빈_상태이면_주문할_수_없다() { // TODO: 채우기
        //given

        //when

        //then
    }
}
