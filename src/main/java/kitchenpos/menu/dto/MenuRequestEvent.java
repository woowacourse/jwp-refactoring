package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.domain.Menu;

public class MenuRequestEvent {

    private final Menu menu;
    private final List<Long> productIds;
    private final long quantity;

    public MenuRequestEvent(Menu menu, MenuRequest menuRequest) {
        this.menu = menu;
        this.productIds = menuRequest.getProductIds();
        this.quantity = menuRequest.getQuantity();
    }

    public Menu getMenu() {
        return menu;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public long getQuantity() {
        return quantity;
    }
}
