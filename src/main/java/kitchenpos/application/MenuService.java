package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.request.MenuProductCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuCreatedValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuCreatedValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCommand menuCommand) {
        menuValidator.validate(menuCommand);
        return MenuResponse.from(saveMenuBelongMenuProduct(menuCommand));
    }

    private Menu saveMenuBelongMenuProduct(MenuCommand menuCommand) {
        Menu savedMenu = menuRepository.save(new Menu(menuCommand.getName(), menuCommand.getPrice(), menuCommand.getMenuGroupId()));
        for (MenuProductCommand menuProduct : menuCommand.getMenuProducts()) {
            savedMenu.addMenuProduct(new MenuProduct(savedMenu.getId(), menuProduct.getProductId(), menuProduct.getQuantity()));
        }
        return savedMenu;
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
