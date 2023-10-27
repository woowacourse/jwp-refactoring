package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuDto create(final CreateMenuCommand command) {
        Menu menu = command.toDomain();
        menu.register(menuValidator);
        return MenuDto.from(menuRepository.save(menu));
    }

    public List<MenuDto> list() {
        return menuRepository.findAll().stream()
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }

}
