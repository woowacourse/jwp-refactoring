package kitchenpos.menu.application;

import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.request.MenuGroupRequest;
import kitchenpos.menu.ui.response.MenuGroupResponse;
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
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        final MenuGroup menuGroup = new MenuGroup.Builder()
                .name(menuGroupRequest.getName())
                .build();
        menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponse.toList(menuGroups);
    }
}
