package kitchenpos.menu.application;

import kitchenpos.menu.Menu;
import kitchenpos.menuproduct.application.request.MenuProductRequest;

import java.util.List;

public class SaveMenuProductsEvent {
    private final List<MenuProductRequest> requests;
    private final Menu menu;

    public SaveMenuProductsEvent(final List<MenuProductRequest> requests, final Menu menu) {
        this.requests = requests;
        this.menu = menu;
    }

    public List<MenuProductRequest> getRequests() {
        return requests;
    }

    public Menu getMenu() {
        return menu;
    }
}
