package kitchenpos.application.concrete;

import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.mapper.MenuMapper;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaMenuService implements MenuService {
    private final MenuMapper menuMapper;
    private final MenuRepository menuRepository;

    public JpaMenuService(final MenuMapper menuMapper, final MenuRepository menuRepository) {
        this.menuMapper = menuMapper;
        this.menuRepository = menuRepository;
    }

    @Transactional
    @Override
    public Menu create(final MenuCreateRequest request) {
        final var menu = menuMapper.mapFrom(request);

        return menu.create();
    }

    @Override
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
