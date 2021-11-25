package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService
            (
                    MenuValidator menuValidator,
                    MenuRepository menuRepository
            ) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.connectMenu(menu);
        }
        menuValidator.validate(menu);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
