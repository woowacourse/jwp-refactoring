package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuChangedEvent;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuEventHandler {
    private MenuRepository menuRepository;
    private OrderLineItemRepository orderLineItemRepository;

    public MenuEventHandler(
            MenuRepository menuRepository,
            OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(MenuChangedEvent event) {
        Menu savedMenu = menuRepository.save(event.getMenu());
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByMenuId(event.getOriginMenuId());
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeMenuId(savedMenu.getId());
        }
    }
}
