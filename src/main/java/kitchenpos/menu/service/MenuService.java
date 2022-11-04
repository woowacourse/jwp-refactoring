package kitchenpos.menu.service;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.dto.response.MenusResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuMapper menuMapper;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final MenuMapper menuMapper,
                       final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository) {
        this.menuMapper = menuMapper;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        Menu menu = menuMapper.mappingToMenu(request);
        validateInMenuGroup(menu);
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    private void validateInMenuGroup(final Menu menu) {
        Long menuGroupId = menu.getMenuGroupId();
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupNotFoundException("등록되지 않은 메뉴그룹 입니다.");
        }
    }

    public MenusResponse list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenusResponse.from(menus);
    }
}
