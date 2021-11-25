package kitchenpos.order.domain;

public interface OrderValidator {

    void validate(Order order);
    void validateChangeStatus(Order order);
}
