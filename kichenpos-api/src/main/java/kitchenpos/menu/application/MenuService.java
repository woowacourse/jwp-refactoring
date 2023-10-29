package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.CreateMenuDto;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final CreateMenuDto dto) {
        final Menu menu = Menu.createNewMenu(
                dto.getName(),
                dto.getPrice(),
                dto.getMenuGroupId(),
                dto.toMenuProducts(),
                menuValidator
        );

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
