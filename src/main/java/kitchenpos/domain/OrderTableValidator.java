package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.exception.OrderTableException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderDao orderDao;

    public OrderTableValidator(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new OrderTableException("그룹에 속한 테이블은 비어있음 상태를 변경할 수 없습니다.");
        }
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(),
                List.of(COOKING.name(), MEAL.name())
        )) {
            throw new OrderTableException("조리 혹은 식사중 상태의 테이블의 비어있음 상태는 변경할 수 없습니다.");
        }
    }
}
