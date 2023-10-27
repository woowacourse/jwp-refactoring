package kitchenpos.application.menu;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.menu.Menu;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.dto.MenuRequest;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuMapper menuMapper;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator,
            final MenuMapper menuMapper
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final Menu menu = menuMapper.toMenu(request, menuValidator);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
