package kitchenpos.menu_group.application;

import java.util.List;
import kitchenpos.menu_group.repository.MenuGroupRepository;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.dto.MenuGroupRequest;
import kitchenpos.menu_group.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = MenuGroup.builder()
            .name(request.getName())
            .build();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.from(savedMenuGroup);
    }

    @Transactional
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponse.listFrom(menuGroups);
    }
}
