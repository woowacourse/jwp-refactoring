package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.CreateMenuCommand;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.model.menu.Menu;
import kitchenpos.domain.model.menu.MenuCreateService;
import kitchenpos.domain.model.menu.MenuRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCreateService menuCreateService;

    public MenuService(final MenuRepository menuRepository,
            final MenuCreateService menuCreateService) {
        this.menuRepository = menuRepository;
        this.menuCreateService = menuCreateService;
    }

    @Transactional
    public MenuResponse create(final CreateMenuCommand command) {
        Menu menu = command.toEntity();
        Menu saved = menuRepository.save(menu.create(menuCreateService));
        return MenuResponse.of(saved);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }
}
