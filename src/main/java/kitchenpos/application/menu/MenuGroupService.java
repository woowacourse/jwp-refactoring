package kitchenpos.application.menu;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.menu.CreateMenuGroupRequest;
import kitchenpos.dto.menu.ListMenuGroupResponse;
import kitchenpos.dto.menu.MenuGroupResponse;
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
