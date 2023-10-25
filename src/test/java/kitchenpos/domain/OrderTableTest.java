package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.product.domain.Price;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 테이블_수정_가능하다() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.setEmpty(false);
        Order order = new Order(orderTable);

        assertThatThrownBy(() -> orderTable.setEmpty(true)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 완료 상태일때만 테이블 수정 가능합니다.");
    }

    @Test
    void 주문_완료_상태일때만_테이블_수정_가능하다() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.setEmpty(false);
        Order order = new Order(orderTable);
        OrderLineItem orderLineItem = new OrderLineItem(
                order,
                new Menu("로제떡볶이", Price.of(10000), new MenuGroup("분식")),
                2
        );
        order.setOrderStatus(OrderStatus.COMPLETION);

        orderTable.setEmpty(true);

        Assertions.assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void 테이블_그룹이_존재하지_않는경우_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.attachTableGroup(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹이 존재하지 않는 경우 예외가 발생한다.");
    }

    @Test
    void 주문_테이블을_합칠_수_있다() {
        OrderTable orderTable = new OrderTable(0, true);
        TableGroup tableGroup = new TableGroup();
        orderTable.attachTableGroup(tableGroup);
        orderTable.isEmpty();
    }

    @Test
    void 테이블_분리_가능하다() {
        OrderTable orderTable = new OrderTable(0, true);
        TableGroup tableGroup = new TableGroup();
        orderTable.attachTableGroup(tableGroup);

        orderTable.detachTableGroup();
        Assertions.assertThat(orderTable.getTableGroup()).isNull();
    }

    @Test
    void 주문_완료_상태일때만_테이블_분리_가능하다() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.setEmpty(false);
        Order order = new Order(orderTable);

        assertThatThrownBy(() -> orderTable.detachTableGroup()).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 완료 상태일때만 테이블 수정 가능합니다.");
    }

    @Test
    void 손님_수는_음수일_수_없습니다() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
    }

    @Test
    void 손님_수를_변경할_수_있다() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.setEmpty(false);

        orderTable.changeNumberOfGuests(2);
        Assertions.assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }
}
