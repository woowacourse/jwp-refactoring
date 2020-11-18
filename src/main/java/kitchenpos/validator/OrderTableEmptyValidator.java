package kitchenpos.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kitchenpos.repository.OrderTableRepository;

public class OrderTableEmptyValidator
    implements ConstraintValidator<OrderTableEmptyValidate, Long> {
    private final OrderTableRepository orderTableRepository;

    public OrderTableEmptyValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void initialize(OrderTableEmptyValidate constraint) {
    }

    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return orderTableRepository.findById(value)
            .filter(table -> !table.isEmpty())
            .isPresent();
    }
}
