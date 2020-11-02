package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupRequest.toMenuGroup();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.of(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> foundMenuGroups = menuGroupRepository.findAll();

        return MenuGroupResponse.listOf(foundMenuGroups);
    }
}
