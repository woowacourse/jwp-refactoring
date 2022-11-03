package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuGroupsResponse;
import kitchenpos.dao.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = new MenuGroup(request.getName());
        menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.from(menuGroup);
    }

    public MenuGroupsResponse list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupsResponse.from(menuGroups);
    }
}
