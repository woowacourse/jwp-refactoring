package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final kitchenpos.menu.application.MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final kitchenpos.menu.application.MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public Menu create(final Menu menu) {
        menuValidator.validate(menu);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
