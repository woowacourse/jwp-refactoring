package kitchenpos.menu.service;

import java.util.List;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService(
        final MenuValidator menuValidator,
        final MenuRepository menuRepository
    ) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        Menu createdMenu = menuRepository.save(menu);

        menuValidator.validate(createdMenu);

        return createdMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public Menu getById(Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(() -> new CustomException(ExceptionType.MENU_NOT_FOUND));
    }
}
