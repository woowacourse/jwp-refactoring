package kitchenpos.application;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.exception.OrderNotCompleteException;
import kitchenpos.repository.OrderRepository;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class OrderStatusValidator implements ConstraintValidator<OrderStatusValidate, Object[]>{
    private final OrderRepository orderRepository;

    public OrderStatusValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (value[0] == null || value[1] == null) {
            return false;
        }

        if (!(value[0] instanceof Long) || !(value[1] instanceof OrderTableChangeEmptyRequest)) {
            return false;
        }

        return !orderRepository.existsByOrderTableIdAndOrderStatusIn(
            (Long)value[0], Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
