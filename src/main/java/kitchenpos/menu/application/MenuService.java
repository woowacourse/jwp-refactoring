package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    public MenuService(final MenuRepository menuRepository, final MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Menu menu = menuMapper.from(request);
        Menu savedMenu = menuRepository.save(menu);
        return new MenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
