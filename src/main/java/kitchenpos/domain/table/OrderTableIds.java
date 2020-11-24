package kitchenpos.domain.table;

import kitchenpos.exception.InvalidOrderTableIdsException;
import kitchenpos.util.ValidateUtil;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class OrderTableIds {
    private static final int MIN_COUNT_OF_ORDER_TABLE = 2;

    private final List<Long> orderTableIds;

    private OrderTableIds(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static OrderTableIds from(List<Long> orderTableIds) {
        ValidateUtil.validateNonNull(orderTableIds);
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_COUNT_OF_ORDER_TABLE) {
            throw new InvalidOrderTableIdsException("단체 지정 생성 시 소속된 주문 테이블이 " + MIN_COUNT_OF_ORDER_TABLE + "개 이상이어야 합니다!");
        }

        return new OrderTableIds(orderTableIds);
    }

    public boolean isNotEqualSize(long size) {
        return this.orderTableIds.size() != size;
    }

    public List<Long> getOrderTableIds() {
        return Collections.unmodifiableList(orderTableIds);
    }
}
