package kitchenpos.application.validator;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.util.List;
import java.util.function.Supplier;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class BasicTableValidator implements TableValidator {

    private static final List<String> NONE_EMPTY_ORDER_TABLE = List.of(COOKING.name(), OrderStatus.MEAL.name());

    private final OrderDao orderDao;

    public BasicTableValidator(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Supplier<Boolean> validate(final OrderTable ordertable) {
        return () -> orderDao.existsByOrderTableIdAndOrderStatusIn(ordertable.getId(), NONE_EMPTY_ORDER_TABLE);
    }
}
