package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuPrice;
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
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuCommand menuCommand) {
        return MenuResponse.from(menuRepository.save(
                        Menu.create(
                                menuCommand.getName(),
                                new MenuPrice(menuCommand.getPrice()),
                                menuCommand.getMenuGroupId(),
                                toMenuProducts(menuCommand),
                                menuValidator)
                )
        );
    }

    private List<MenuProduct> toMenuProducts(MenuCommand menuCommand) {
        return menuCommand.getMenuProducts().stream()
                .map(MenuProductCommand::toEntity)
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
