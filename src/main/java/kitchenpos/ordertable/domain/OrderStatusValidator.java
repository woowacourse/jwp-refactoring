package kitchenpos.ordertable.domain;

public interface OrderStatusValidator {
    
    public void validateOrderStatusNotCompleted(Long tableId);
}
