package kitchenpos.application.menu;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuMapper menuMapper, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(MenuRequest request) {
        Menu menu = menuMapper.toMenu(request);
        menu.register(menuValidator);
        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(toList());
    }
}
