package kitchenpos.ordertable.application;

public interface OrderStatusValidator {
    
    public void validateOrderStatusNotCompleted(Long tableId);
}
