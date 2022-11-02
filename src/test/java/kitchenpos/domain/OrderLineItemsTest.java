package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void 주문_항목이_비어있으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> new OrderLineItems(new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목_수와_메뉴_수가_같지_않으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> {
                    OrderTable 주문_테이블 = new OrderTable(null, 3, false);
                    Order 요리중_주문 = new Order(주문_테이블, OrderStatus.COOKING.name(), LocalDateTime.now());
                    OrderLineItems orderLineItems = new OrderLineItems(
                            Arrays.asList(new OrderLineItem(요리중_주문, 1L, 1)));

                    orderLineItems.validateMenuSize(2L);
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
