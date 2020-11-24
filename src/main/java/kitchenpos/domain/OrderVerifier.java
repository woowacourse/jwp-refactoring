package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderVerifier {
    public void verify(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderTable) || orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 존재해야 합니다.");
        }
    }
}
