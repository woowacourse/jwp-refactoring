package kitchenpos.order.validator;

import static kitchenpos.order.OrderStatus.COOKING;
import static kitchenpos.order.OrderStatus.MEAL;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import kitchenpos.table.validator.TableGroupValidator;
import kitchenpos.order.OrderRepository;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class BasicTableGroupValidator implements TableGroupValidator {

    private static final List<String> NONE_UNGROUP_ORDER_STATUS = List.of(COOKING.name(), MEAL.name());

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public BasicTableGroupValidator(final OrderRepository orderRepository,
                                    final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public Supplier<Boolean> validate(final TableGroup tableGroup) {
        List<Long> orderTableIds = toOrderTableIds(tableGroup.getId());
        return () -> orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, NONE_UNGROUP_ORDER_STATUS);
    }

    private List<Long> toOrderTableIds(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
