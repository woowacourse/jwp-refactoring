package kitchenpos.application.menu;

import kitchenpos.application.menu.request.MenuGroupCreateRequest;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = mapToMenuGroup(request);
        return menuGroupRepository.save(menuGroup);
    }

    private MenuGroup mapToMenuGroup(MenuGroupCreateRequest request) {
        return new MenuGroup(request.getName());
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
