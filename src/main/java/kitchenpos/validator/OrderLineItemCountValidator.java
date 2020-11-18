package kitchenpos.validator;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.CollectionUtils;

import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.repository.MenuRepository;

public class OrderLineItemCountValidator
    implements ConstraintValidator<OrderLineItemCountValidate, List<OrderLineItemCreateRequest>> {

    private final MenuRepository menuRepository;

    public OrderLineItemCountValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public boolean isValid(List<OrderLineItemCreateRequest> value,
        ConstraintValidatorContext context) {
        if (CollectionUtils.isEmpty(value)) {
            return false;
        }

        List<Long> menuIds = value.stream()
            .map(OrderLineItemCreateRequest::getMenuId)
            .collect(Collectors.toList());
        return menuRepository.countByIdIn(menuIds) == menuIds.size();
    }
}
