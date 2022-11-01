package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.menu.application.dto.request.MenuCommand;
import kitchenpos.menu.application.dto.request.MenuProductCommand;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuCommand menuCommand) {
        String name = menuCommand.getName();
        Price price = new Price(menuCommand.getPrice());
        Long menuGroupId = menuCommand.getMenuGroupId();
        List<MenuProduct> menuProducts = toMenuProducts(menuCommand);

        Menu menu = menuRepository.save(Menu.create(name, price, menuGroupId, menuProducts, menuValidator));
        return MenuResponse.from(menu);
    }

    private List<MenuProduct> toMenuProducts(MenuCommand menuCommand) {
        return menuCommand.getMenuProducts().stream()
                .map(MenuProductCommand::toEntity)
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
