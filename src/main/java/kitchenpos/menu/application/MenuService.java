package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public Menu create(final MenuCreateRequest request) {
        Menu menu = Menu.create(request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                request.getMenuProducts(),
                menuValidator);
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
