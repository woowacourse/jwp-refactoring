package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreationRequest;
import kitchenpos.menu.application.dto.MenuResult;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuMapper menuMapper
    ) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    public MenuResult create(final MenuCreationRequest request) {
        final Menu menu = menuMapper.from(request);
        menuRepository.save(menu);
        return MenuResult.from(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResult> list() {
        return menuRepository.findAll().stream()
                .map(MenuResult::from)
                .collect(Collectors.toList());
    }
}
