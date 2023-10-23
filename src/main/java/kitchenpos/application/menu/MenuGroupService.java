package kitchenpos.application.menu;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.menu.CreateMenuGroupRequest;
import kitchenpos.dto.menu.CreateMenuGroupResponse;
import kitchenpos.dto.menu.ListMenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public CreateMenuGroupResponse create(final CreateMenuGroupRequest request) {
        final MenuGroup menuGroup = MenuGroup.of(request.getName());
        return CreateMenuGroupResponse.of(menuGroupRepository.save(menuGroup));
    }

    public ListMenuGroupResponse list() {
        return ListMenuGroupResponse.of(menuGroupRepository.findAll());
    }
}
