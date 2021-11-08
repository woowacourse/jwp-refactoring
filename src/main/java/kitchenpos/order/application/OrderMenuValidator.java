package kitchenpos.order.application;

import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMenuValidator {
    private final MenuRepository menuRepository;

    public OrderMenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateMenuId(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new IllegalArgumentException();
        }
    }
}
