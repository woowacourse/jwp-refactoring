package kitchenpos.menu.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.request.MenuCreationRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuCreationRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        MenuProducts menuProducts = createMenuProducts(request.getMenuProductRequests());
        Menu menu = createMenu(request, menuGroup, menuProducts);

        menuRepository.save(menu);

        return MenuResponse.from(menu);
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 메뉴 그룹이 존재하지 않습니다."));
    }

    private MenuProducts createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(request -> MenuProduct.create(request.getQuantity(), request.getProductId()))
                .collect(Collectors.toList());

        return MenuProducts.from(menuProducts);
    }

    private Menu createMenu(MenuCreationRequest request, MenuGroup menuGroup, MenuProducts menuProducts) {
        return Menu.create(request.getName(), request.getPrice(), menuGroup, menuProducts, menuValidator);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

}
