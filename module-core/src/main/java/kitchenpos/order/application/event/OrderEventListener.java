package kitchenpos.order.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.menu.application.event.MenuCreatedEvent;
import kitchenpos.order.domain.MenuHistory;
import kitchenpos.order.domain.repository.MenuHistoryRepository;

@Component
public class OrderEventListener {

    private final MenuHistoryRepository menuHistoryRepository;

    public OrderEventListener(final MenuHistoryRepository menuHistoryRepository) {
        this.menuHistoryRepository = menuHistoryRepository;
    }

    @EventListener
    public void saveMenuHistory(final MenuCreatedEvent event) {
        final var createdMenu = event.getMenu();
        menuHistoryRepository.save(MenuHistory.copyOf(createdMenu));
    }
}
