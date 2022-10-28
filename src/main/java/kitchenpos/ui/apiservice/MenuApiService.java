package kitchenpos.ui.apiservice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;

@Service
public class MenuApiService {

    private final MenuService menuService;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuApiService(MenuService menuService, ProductService productService,
                          MenuGroupService menuGroupService) {
        this.menuService = menuService;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }


    public List<MenuResponse> list() {
        List<Menu> menus = menuService.list();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public MenuResponse create(MenuRequest menuRequest) {
        menuRequest.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(it.getProductId(), ))


        Map<Product, Integer> productQuantity = menuRequest.getMenuProducts()
                .stream()
                .collect(Collectors.toMap(
                        product -> productService.search(product.getProductId()),
                        MenuProductRequest::getQuantity
                ));
        MenuGroup menuGroup = menuGroupService.search(menuRequest.getMenuGroupId());
        Menu menu = menuService.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, productQuantity);
        return MenuResponse.of(menu);
    }
}
