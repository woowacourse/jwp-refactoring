package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.CreateMenuCommand;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.application.verifier.CreateMenuVerifier;
import kitchenpos.domain.model.menu.Menu;
import kitchenpos.domain.model.menu.MenuRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final CreateMenuVerifier createMenuVerifier;

    public MenuService(final MenuRepository menuRepository,
            final CreateMenuVerifier createMenuVerifier) {
        this.menuRepository = menuRepository;
        this.createMenuVerifier = createMenuVerifier;
    }

    @Transactional
    public MenuResponse create(final CreateMenuCommand command) {
        Menu menu = createMenuVerifier.toMenu(command.getName(), command.getPrice(),
                command.getMenuGroupId(), command.getMenuProducts());
        Menu saved = menuRepository.save(menu);
        return MenuResponse.of(saved);
    }

    @Transactional
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllWithMenuProducts();
        return MenuResponse.listOf(menus);
    }
}
