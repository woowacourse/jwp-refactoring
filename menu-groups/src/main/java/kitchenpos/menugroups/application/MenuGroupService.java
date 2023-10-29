package kitchenpos.menugroups.application;

import kitchenpos.menugroups.domain.MenuGroup;
import kitchenpos.menugroups.domain.MenuGroupRepository;
import kitchenpos.menugroups.dto.CreateMenuGroupRequest;
import kitchenpos.menugroups.dto.ListMenuGroupResponse;
import kitchenpos.menugroups.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final CreateMenuGroupRequest request) {
        final MenuGroup menuGroup = MenuGroup.of(request.getName());
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    public ListMenuGroupResponse list() {
        return ListMenuGroupResponse.from(menuGroupRepository.findAll());
    }
}
