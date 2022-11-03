package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderValidator(MenuRepository menuRepository, OrderDao orderDao, OrderLineItemDao orderLineItemDao,
                          OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    public void validate(Long orderTableId, List<OrderLineItem> orderLineItems) {
        checkExistOrderTable(orderTableId);
        checkOrderLineItemsIsNotNull(orderLineItems);
        checkExistMenu(orderLineItems);
    }

    private void checkOrderLineItemsIsNotNull(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkExistMenu(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkExistOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
