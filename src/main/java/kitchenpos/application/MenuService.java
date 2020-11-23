package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuProductService menuProductService;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuProductService menuProductService, MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository) {
        this.menuProductService = menuProductService;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 Menu Group Id 입니다.");
        }

        Menu savedMenu = menuRepository.save(menuRequest.toMenu());
        List<MenuProduct> menuProduct = menuProductService.createMenuProduct(savedMenu, menuRequest.getMenuProductRequests());
        return MenuResponse.of(savedMenu, menuProduct);
    }

    public List<MenuResponse> list() {
        List<MenuProduct> menuProducts = menuProductService.findAll();
        Set<Menu> menus = extractDistinctMenu(menuProducts);
        return menus.stream()
                .map(menu -> MenuResponse.of(menu, selectiveByMenu(menu, menuProducts)))
                .collect(Collectors.toList());
    }

    private Set<Menu> extractDistinctMenu(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getMenu)
                .collect(Collectors.toSet());
    }

    private List<MenuProduct> selectiveByMenu(Menu menu, List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .filter(menuProduct -> menuProduct.equalsByMenuId(menu.getId()))
                .collect(Collectors.toList());
    }

}
