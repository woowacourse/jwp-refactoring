package kitchenpos.application.validator;

import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderStatus.COOKING;

import java.util.List;
import java.util.function.Supplier;
import kitchenpos.dao.OrderDao;
import org.springframework.stereotype.Component;

@Component
public class BasicTableValidator implements TableValidator {

    private static final List<String> NONE_EMPTY_ORDER_TABLE = List.of(COOKING.name(), MEAL.name());

    private final OrderDao orderDao;

    public BasicTableValidator(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Supplier<Boolean> validate(final Long orderTableId) {
        return () -> orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, NONE_EMPTY_ORDER_TABLE);
    }
}
