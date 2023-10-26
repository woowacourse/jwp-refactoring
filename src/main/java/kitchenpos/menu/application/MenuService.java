package kitchenpos.menu.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.common.dto.request.MenuCreationRequest;
import kitchenpos.common.dto.request.MenuProductRequest;
import kitchenpos.common.dto.response.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.common.dto.response.MenuGroupResponse;
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
        MenuProducts menuProducts = createMenuProducts(request.getMenuProductRequests());
        Menu menu = createMenu(request, request.getMenuGroupId(), menuProducts);

        menuRepository.save(menu);

        return mapToMenuResponse(menu);
    }

    private MenuProducts createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(request -> MenuProduct.create(request.getQuantity(), request.getProductId()))
                .collect(Collectors.toList());

        return MenuProducts.from(menuProducts);
    }

    private Menu createMenu(MenuCreationRequest request, Long menuGroupId, MenuProducts menuProducts) {
        return Menu.create(request.getName(), request.getPrice(), menuGroupId, menuProducts, menuValidator);
    }

    private MenuResponse mapToMenuResponse(Menu menu) {
        MenuGroupResponse menuGroupResponse = mapToMenuGroupResponse(menu);

        return MenuResponse.from(menu, menuGroupResponse);
    }

    private MenuGroupResponse mapToMenuGroupResponse(Menu menu) {
        MenuGroup menGroup = findMenGroup(menu.getMenuGroupId());

        return MenuGroupResponse.from(menGroup);
    }

    private MenuGroup findMenGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 메뉴 그룹이 존재하지 않습니다."));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(this::mapToMenuResponse)
                .collect(Collectors.toList());
    }

}
