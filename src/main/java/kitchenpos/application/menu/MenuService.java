package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuMapper;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.CreateMenuResponse;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuMapper menuMapper;
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuMapper menuMapper,
            final MenuRepository menuRepository,
            final MenuValidator menuValidator) {
        this.menuMapper = menuMapper;
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public CreateMenuResponse create(final CreateMenuRequest request) {
        final Menu menu = menuMapper.toMenu(request);
        menuValidator.validate(menu);
        return CreateMenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
