package core.application.menu;

import core.application.dto.MenuRequest;
import core.domain.Menu;
import core.domain.MenuRepository;
import core.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuMapper menuMapper;

    public MenuService(
            final MenuRepository menuRepository,
            MenuValidator menuValidator, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public Menu create(MenuRequest request) {
        Menu menu = menuMapper.toMenu(request, menuValidator);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
