package kitchenpos.ui.apiservice;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;

@Service
public class MenuApiService {

    private final MenuService menuService;

    public MenuApiService(MenuService menuService) {
        this.menuService = menuService;
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuService.list();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public MenuResponse create(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(
                        it.getProductId(),
                        it.getQuantity())
                ).collect(Collectors.toList());
        Menu menu = menuService.create(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                menuProducts);
        return MenuResponse.of(menu);
    }
}
