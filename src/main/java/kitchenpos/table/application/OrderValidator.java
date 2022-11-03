package kitchenpos.table.application;

import java.util.List;

public interface OrderValidator {

    void validateNotCookingAndMeal(final List<Long> orderTableIds);
}
