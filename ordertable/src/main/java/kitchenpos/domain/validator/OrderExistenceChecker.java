package kitchenpos.domain.validator;

public interface OrderExistenceChecker {
    boolean hasCookingOrMealOrderByOrderTableId(Long orderTableId);
}
