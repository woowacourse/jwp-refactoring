package kitchenpos.menu.service;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.service.dto.MenuRequest;
import kitchenpos.menu.service.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final Menu menu = MenuMapper.toMenu(request);
        menu.place(menuValidator);
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
