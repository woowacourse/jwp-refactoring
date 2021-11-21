package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MenuProductEventHandler {
    private final MenuProductRepository menuProductRepository;
    private final MenuRepository menuRepository;

    public MenuProductEventHandler(MenuProductRepository menuProductRepository,
                                   MenuRepository menuRepository) {
        this.menuProductRepository = menuProductRepository;
        this.menuRepository = menuRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(MenuProductEvent event) {
        final MenuProduct menuProduct = menuProductRepository.findByProductId(event.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        final Menu menu = menuRepository.findById(menuProduct.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        final Price productPrice = new Price(event.getProductPrice());
        menu.updatePrice(productPrice.multiply(menuProduct.getQuantity()));
    }
}
