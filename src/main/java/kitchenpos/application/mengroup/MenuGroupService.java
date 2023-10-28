package kitchenpos.application.mengroup;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.dto.menugroup.CreateMenuGroupRequest;
import kitchenpos.dto.menugroup.ListMenuGroupResponse;
import kitchenpos.dto.menugroup.MenuGroupResponse;
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
