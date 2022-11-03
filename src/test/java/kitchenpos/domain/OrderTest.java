package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    void 주문_항목들이_없으면_주문을_생성할_수_없다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable = new OrderTable(tableGroup.getId(), 3, true);

        assertThatThrownBy(
                () -> new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 배달이_완료된_상태를_변경하면_예외가_발생한다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable = new OrderTable(tableGroup.getId(), 3, false);

        assertThatThrownBy(
                () -> {
                    Order order = new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now());
                    order.validateStatusForChange();
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
