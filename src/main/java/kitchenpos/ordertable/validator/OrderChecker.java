package kitchenpos.ordertable.validator;

public interface OrderChecker {

    boolean isNotCompletionOrder(Long orderTableId);
}
