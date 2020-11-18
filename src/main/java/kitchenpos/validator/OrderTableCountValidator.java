package kitchenpos.validator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kitchenpos.dto.request.OrderTableId;
import kitchenpos.repository.OrderTableRepository;

public class OrderTableCountValidator
    implements ConstraintValidator<OrderTableCountValidate, List<OrderTableId>> {
    private final OrderTableRepository orderTableRepository;

    public OrderTableCountValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public boolean isValid(List<OrderTableId> value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }

        if (value.size() < 2) {
            return false;
        }

        List<Long> ids = value.stream()
            .map(OrderTableId::getId)
            .collect(Collectors.toList());
        return orderTableRepository.countByIdIn(ids) == value.size();
    }
}
