package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.ui.dto.response.MenuGroupCreateResponse;
import kitchenpos.menu.ui.dto.response.MenuGroupFindAllResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupCreateResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return new MenuGroupCreateResponse(savedMenuGroup.getId(), savedMenuGroup.getName());
    }

    public List<MenuGroupFindAllResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupFindAllResponse.from(menuGroups);
    }
}
