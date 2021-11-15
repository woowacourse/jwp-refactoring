package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuService {

    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService(MenuValidator menuValidator, MenuRepository menuRepository) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest request) {
        Menu menu = request.toEntity();
        menuValidator.validate(menu);
        menuRepository.save(menu);
        return MenuResponse.of(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }
}
