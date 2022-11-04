package kitchenpos.order.validator;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import kitchenpos.table.validator.TableGroupValidator;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class BasicTableGroupValidator implements TableGroupValidator {

    private static final List<String> NONE_UNGROUP_ORDER_STATUS = List.of(COOKING.name(), MEAL.name());

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public BasicTableGroupValidator(final OrderDao orderDao,
                                    final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Override
    public Supplier<Boolean> validate(final TableGroup tableGroup) {
        List<Long> orderTableIds = toOrderTableIds(tableGroup.getId());
        return () -> orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, NONE_UNGROUP_ORDER_STATUS);
    }

    private List<Long> toOrderTableIds(final Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
