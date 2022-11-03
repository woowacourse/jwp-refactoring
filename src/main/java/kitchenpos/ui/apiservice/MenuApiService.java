package kitchenpos.ui.apiservice;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuApiService {

    private final MenuService menuService;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuApiService(MenuService menuService, MenuGroupService menuGroupService, ProductService productService) {
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuService.list();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.search(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(
                        null,
                        productService.search(it.getProductId()),
                        it.getQuantity())
                ).collect(Collectors.toList());
        Menu menu = menuService.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        return MenuResponse.of(menu);
    }
}
