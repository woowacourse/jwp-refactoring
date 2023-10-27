package kitchenpos.menu;

import kitchenpos.menugroup.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
