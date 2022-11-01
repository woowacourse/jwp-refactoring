package kitchenpos.domain.validator;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.badrequest.OrderTableAlreadyInGroupException;
import kitchenpos.exception.badrequest.OrderTableNegativeNumberOfGuestsException;
import kitchenpos.exception.badrequest.OrderTableUnableToChangeNumberOfGuestsWhenEmptyException;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {
    private static final List<OrderStatus> ACTIVE_ORDER_STATUS = List.of(OrderStatus.COOKING, OrderStatus.MEAL);

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(final OrderTable orderTable, final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, ACTIVE_ORDER_STATUS)) {
            throw new IllegalArgumentException();
        }

        if (orderTable.alreadyInGroup()) {
            throw new OrderTableAlreadyInGroupException(orderTable.getId());
        }
    }

    public void validateChangeNumberOfGuests(final OrderTable orderTable, final int numberOfGuests) {
        if (orderTable.isEmpty()) {
            throw new OrderTableUnableToChangeNumberOfGuestsWhenEmptyException();
        }

        if (numberOfGuests < 0) {
            throw new OrderTableNegativeNumberOfGuestsException(numberOfGuests);
        }
    }

    public void validateGroup(final OrderTable orderTable) {
        if (orderTable.alreadyInGroup() || !orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateUnGroup(final OrderTable orderTable) {
        if (!orderTable.alreadyInGroup() || orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
