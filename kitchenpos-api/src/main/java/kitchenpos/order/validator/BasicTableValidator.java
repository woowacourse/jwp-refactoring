package kitchenpos.order.validator;

import static kitchenpos.order.OrderStatus.*;
import static kitchenpos.order.OrderStatus.COOKING;

import java.util.List;
import java.util.function.Supplier;
import kitchenpos.table.validator.TableValidator;
import kitchenpos.order.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class BasicTableValidator implements TableValidator {

    private static final List<String> NONE_EMPTY_ORDER_TABLE = List.of(COOKING.name(), MEAL.name());

    private final OrderRepository orderRepository;

    public BasicTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Supplier<Boolean> validate(final Long orderTableId) {
        return () -> orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, NONE_EMPTY_ORDER_TABLE);
    }
}
