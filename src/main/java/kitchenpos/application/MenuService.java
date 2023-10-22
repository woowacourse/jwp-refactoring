package kitchenpos.application;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.application.dto.MenuRequest.MenuProductRequest;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(MenuRequest request) {
        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        addMenuProducts(request, menu);
        return menuRepository.save(menu);
    }

    private void addMenuProducts(MenuRequest request, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            MenuProduct menuProduct = new MenuProduct(menuProductRequest.getProductId(), menuProductRequest.getQuantity());
            menuProducts.add(menuProduct);
        }
        menu.changeMenuProducts(menuProducts, menuValidator);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
