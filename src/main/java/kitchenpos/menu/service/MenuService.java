package kitchenpos.menu.service;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuMapper menuMapper;

    public MenuService(MenuRepository menuRepository,
                       MenuValidator menuValidator, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuMapper.mapFrom(menuRequest);
        menu.validate(menuValidator);
        menuRepository.save(menu);
        return MenuResponse.of(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }
}
