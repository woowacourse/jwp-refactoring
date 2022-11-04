package kitchenpos.domain.validator;

import kitchenpos.domain.OrderTable;
import kitchenpos.exception.badrequest.OrderTableAlreadyInGroupException;
import kitchenpos.exception.badrequest.OrderTableNegativeNumberOfGuestsException;
import kitchenpos.exception.badrequest.OrderTableUnableToChangeNumberOfGuestsWhenEmptyException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {
    private final OrderExistenceChecker orderExistenceChecker;

    public OrderTableValidator(final OrderExistenceChecker orderExistenceChecker) {
        this.orderExistenceChecker = orderExistenceChecker;
    }

    public void validateChangeEmpty(final OrderTable orderTable, final Long orderTableId) {
        if (orderExistenceChecker.hasCookingOrMealOrderByOrderTableId(orderTableId)) {
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
