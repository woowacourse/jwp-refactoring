package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.application.dto.request.MenuCommand;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuCommand menuCommand) {
        Menu menu = Menu.create(menuCommand.name(),
                menuCommand.price(),
                menuCommand.menuGroupId(),
                menuCommand.toRawMenuProducts(),
                menuValidator);
        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .toList();
    }
}
