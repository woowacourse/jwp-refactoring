package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.dto.menu.request.MenuProductRequest;
import kitchenpos.dto.menu.request.MenuRequest;
import kitchenpos.dto.menu.response.MenuResponse;
import kitchenpos.repository.menu.MenuGroupRepository;
import kitchenpos.repository.menu.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Menu Group Id 입니다."));
        Menu savedMenu = menuRepository.save(menuRequest.toMenu(menuGroup));
        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();
        List<MenuProduct> menuProduct = menuProductService.createMenuProduct(savedMenu, menuProductRequests);
        return MenuResponse.of(savedMenu, menuProduct);
    }

    public List<MenuResponse> list() {
        MenuProducts menuProducts = new MenuProducts(menuProductService.findAll());
        return menuProducts.createMenuResponses();
    }
}
