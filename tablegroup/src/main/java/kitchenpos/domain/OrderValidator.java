package kitchenpos.domain;

public interface OrderValidator {

    void validateOrderStatusInCookingOrMeal(final OrderTables orderTables);
}
