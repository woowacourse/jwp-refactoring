package kitchenpos.application;

import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.ui.request.MenuGroupCreateRequest;
import kitchenpos.ui.response.MenuGroupResponse;
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
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponse.from(menuGroups);
    }
}
