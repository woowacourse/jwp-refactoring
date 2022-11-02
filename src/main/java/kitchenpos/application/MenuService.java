package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuValidator;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        menuValidator.validatePriceByProducts(menuRequest.getPrice(), menuRequest.getMenuProducts(), menuRequest);

        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuRequest.getMenuProducts());
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = createMenuProducts(menuRequest, savedMenu);
        return new Menu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getMenuGroup(), savedMenuProducts);
    }

    private List<MenuProduct> createMenuProducts(MenuRequest menuRequest, Menu savedMenu) {
        return menuRequest.getMenuProducts()
                .stream()
                .map(menuProduct -> menuProductRepository.save(
                        new MenuProduct(findMenu(savedMenu), menuProduct.getProductId(), menuProduct.getQuantity())))
                .collect(Collectors.toList());
    }

    private Menu findMenu(Menu menu) {
        return menuRepository.findById(menu.getId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
