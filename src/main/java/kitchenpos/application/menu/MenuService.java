package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuMapper;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuMapper menuMapper;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator, final MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final Menu menu = menuMapper.toMenu(request);
        menuValidator.validate(menu);

        return MenuResponse.toResponse(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::toResponse)
                .collect(Collectors.toList());
    }
}
