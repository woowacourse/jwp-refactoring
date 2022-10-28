package kitchenpos.application;

import java.util.Arrays;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderDao orderDao;

    public TableValidator(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void validateChangeEmpty(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 단체지정이 되어있습니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                savedOrderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중이거나 식사 상태입니다.");
        }
    }

    public void validateNumberOfGuests(int numberOfGuests, OrderTable savedOrderTable) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 0 이상이어야 합니다.");
        }
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }

    }
}
