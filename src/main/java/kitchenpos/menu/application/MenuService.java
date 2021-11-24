package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.ui.dto.request.MenuChangeRequest;
import kitchenpos.menu.ui.dto.request.MenuRequest;
import kitchenpos.menu.ui.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuValidator.validateMenuGroup(request.getMenuGroupId());

        final Menu menu = Menu.create(request.getName(), request.getPrice(), request.getMenuGroupId());
        final List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(item -> MenuProduct.create(menu, item.getProductId(), item.getQuantity()))
                .collect(toList());
        menu.addMenuProducts(menuProducts, menuValidator);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.create(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::create)
                .collect(toList());
    }

    @Transactional
    public void changeNamePrice(Long menuId, MenuChangeRequest request) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("MenuId : " + menuId + "는 존재하지 않습니다."));
        menu.changeNameAndPrice(request.getName(), request.getPrice());
        menuRepository.save(menu);
    }
}
