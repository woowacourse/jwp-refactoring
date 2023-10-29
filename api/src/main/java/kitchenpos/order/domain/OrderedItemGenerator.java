package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.vo.Price;
import org.springframework.stereotype.Component;

@Component
public class OrderedItemGenerator {

    private final MenuRepository menuRepository;

    public OrderedItemGenerator(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public OrderedItem generate(final Long menuId) {
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        return new OrderedItem(menu.getId(), menu.getName(), Price.from(menu.getPrice()));
    }
}
