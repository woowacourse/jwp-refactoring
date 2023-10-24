package kitchenpos.application.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class OrderServiceFixture {

    protected Order 저장된_주문;
    protected Order 저장된_주문1;
    protected Order 저장된_주문2;
    protected OrderTable 주문_테이블;
    protected Order 식사_상태의_저장된_주문;
    protected Order 계산_상태의_저장된_주문;
    protected OrderTable 빈_주문_테이블;
    protected String 주문_상태 = OrderStatus.COOKING.name();
    protected List<OrderLineItem> 주문_항목들;
    protected long 주문_항목_수와_다른_개수;
    protected long 존재하지_않는_주문_테이블_아이디 = -999L;
    protected List<Order> 저장된_주문들;

    @BeforeEach
    void setUp() {
        OrderLineItem 주문_항목1 = new OrderLineItem(null, null, 2);
        OrderLineItem 주문_항목2 = new OrderLineItem(null, null, 2);
        주문_항목들 = List.of(주문_항목1, 주문_항목2);
        주문_항목_수와_다른_개수 = Long.parseLong(String.valueOf(주문_항목들.size() - 1));

        주문_테이블 = new OrderTable(2, false);
        주문_테이블.updateTableGroup(null);
        주문_테이블.setId(1L);
        빈_주문_테이블 = new OrderTable(2, true);
        빈_주문_테이블.updateTableGroup(null);
        빈_주문_테이블.setId(2L);

        저장된_주문 = new Order(주문_테이블, 주문_상태, LocalDateTime.now());
        저장된_주문.addOrderLineItems(주문_항목들);
        저장된_주문.setId(1L);
        저장된_주문1 = 저장된_주문;
        저장된_주문2 = new Order(주문_테이블, 주문_상태, LocalDateTime.now());
        저장된_주문2.addOrderLineItems(주문_항목들);
        저장된_주문.setId(2L);
        식사_상태의_저장된_주문 = new Order(주문_테이블, OrderStatus.MEAL.name(), LocalDateTime.now());
        식사_상태의_저장된_주문.addOrderLineItems(주문_항목들);
        식사_상태의_저장된_주문.setId(저장된_주문.getId());
        계산_상태의_저장된_주문 = new Order(주문_테이블, OrderStatus.COMPLETION.name(), LocalDateTime.now());
        계산_상태의_저장된_주문.addOrderLineItems(주문_항목들);
        계산_상태의_저장된_주문.setId(저장된_주문.getId());

        저장된_주문들 = List.of(저장된_주문1, 저장된_주문2);
    }
}
