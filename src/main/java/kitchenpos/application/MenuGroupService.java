package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.vo.menugroup.MenuGroupRequest;
import kitchenpos.vo.menugroup.MenuGroupResponse;
import kitchenpos.vo.menugroup.MenuGroupsResponse;
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
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.of(savedMenuGroup);
    }

    public MenuGroupsResponse list() {
        List<MenuGroup> savedMenuGroup = menuGroupRepository.findAll();

        return MenuGroupsResponse.of(savedMenuGroup);
    }
}
