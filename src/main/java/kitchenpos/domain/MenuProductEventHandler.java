package kitchenpos.domain;

import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuProductEventHandler {
    private final MenuProductRepository menuProductRepository;
    private final MenuRepository menuRepository;

    public MenuProductEventHandler(MenuProductRepository menuProductRepository,
                                   MenuRepository menuRepository) {
        this.menuProductRepository = menuProductRepository;
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void handle(MenuProductEvent event) {
        final MenuProduct menuProduct = menuProductRepository.findByProductId(event.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        final Menu menu = menuRepository.findById(menuProduct.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        final Price productPrice = new Price(event.getProductPrice());
        menu.updatePrice(productPrice.multiply(menuProduct.getQuantity()));
    }
}
