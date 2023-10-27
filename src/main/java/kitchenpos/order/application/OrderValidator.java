package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.dto.OrderCreateRequest;

import java.util.List;

public interface OrderValidator {
    void validate(final OrderCreateRequest request, final List<Menu> menus);
}
