package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuProductRequest;
import kitchenpos.menu.application.dto.request.MenuRequest;
import kitchenpos.menu.application.dto.response.MenuProductResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuRepository menuRepository,
            MenuProductRepository menuProductRepository,
            MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = extractMenuProdcuts(menuRequest);

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId());
        menuValidator.validate(menu, menuProducts);

        menuRepository.save(menu);
        saveMenuProducts(menuProducts, menu);

        return MenuResponse.of(menu, menuProducts);
    }

    private List<MenuProduct> extractMenuProdcuts(MenuRequest menuRequest) {
        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        return createMenuProducts(menuProductRequests);
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            menuProducts.add(new MenuProduct(menuProductRequest.getProductId(), menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private void saveMenuProducts(List<MenuProduct> menuProducts, Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(menu);
            menuProductRepository.save(menuProduct);
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuResponse> menuResponse = new ArrayList<>();

        for (Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            List<MenuProductResponse> menuProductResponses = menuProducts.stream()
                    .map(MenuProductResponse::toDto)
                    .collect(Collectors.toList());
            menuResponse.add(new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProductResponses));
        }

        return menuResponse;
    }
}
