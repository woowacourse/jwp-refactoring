package domain;


import exception.OrderException;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION;

    public OrderStatus changeOrderStatus(final OrderStatus orderStatus) {
        if (this.equals(COMPLETION)) {
            throw new OrderException.CompletionOrderChangeStatusException();
        }
        return orderStatus;
    }
}
