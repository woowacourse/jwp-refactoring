package kitchenpos.menu.service;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.dto.response.MenusResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuMapper menuMapper;
    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService(final MenuMapper menuMapper,
                       final MenuValidator menuValidator,
                       final MenuRepository menuRepository) {
        this.menuMapper = menuMapper;
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        Menu menu = menuMapper.mappingToMenu(request);
        menu.validate(it -> menuValidator.validateInMenuGroup((Menu) it));
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    public MenusResponse list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenusResponse.from(menus);
    }
}
