package kitchenpos.application;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.dto.menugroup.MenuGroupsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.of(savedMenuGroup);
    }

    public MenuGroupsResponse list() {
        List<MenuGroup> savedMenuGroup = menuGroupRepository.findAll();

        return MenuGroupsResponse.of(savedMenuGroup);
    }
}
