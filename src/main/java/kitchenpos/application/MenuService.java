package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductService menuProductService;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductService menuProductService) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public Menu create(MenuRequest menuRequest) {

        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuRepository.save(menuRequest.toMenu());
        menuProductService.createMenuProduct(savedMenu, menuRequest.getMenuProductDtos());
        return savedMenu;
    }

    public List<MenuResponse> list() {
        List<MenuProduct> menuProducts = menuProductService.findAll();
        Set<Menu> menus = menuProducts.stream()
                .map(MenuProduct::getMenu)
                .collect(Collectors.toSet());
        List<MenuResponse> menuResponses = new ArrayList<>();
        for (Menu menu : menus) {
            List<MenuProduct> menuProductsByMenu = menuProducts.stream()
                    .filter(menuProduct -> menuProduct.equalsByMenuId(menu.getId()))
                    .collect(Collectors.toList());
            menuResponses.add(MenuResponse.of(menu, menuProductsByMenu));
        }

        return menuResponses;
    }
}
