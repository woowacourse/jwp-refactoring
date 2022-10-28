package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.request.OrderLineItemCommand;
import kitchenpos.domain.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateCreate(OrderCommand orderCommand) {
        List<OrderLineItemCommand> orderLineItems = orderCommand.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemCommand::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
        }
    }
}
