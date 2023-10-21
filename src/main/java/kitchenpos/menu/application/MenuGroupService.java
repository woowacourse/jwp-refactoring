package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        final List<MenuGroupResponse> menuGroupResponses = new ArrayList<>();
        for (final MenuGroup menuGroup : menuGroups) {
            menuGroupResponses.add(new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()));
        }

        return menuGroupResponses;
    }
}
