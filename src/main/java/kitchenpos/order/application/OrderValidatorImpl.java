package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidatorImpl implements OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(final OrderCreateRequest request, final List<Menu> menus) {
        final OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (request.extractMenuIds().size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }
}
