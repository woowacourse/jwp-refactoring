package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMenuCreator {

    private final MenuRepository menuRepository;

    public OrderMenuCreator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public OrderMenu create(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
        return new OrderMenu(menu.getName(), menu.getPrice().getValue());
    }
}
