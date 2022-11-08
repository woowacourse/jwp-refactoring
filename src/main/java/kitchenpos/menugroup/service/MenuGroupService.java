package kitchenpos.menugroup.service;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.response.MenuGroupResponse;
import kitchenpos.menugroup.dto.response.MenuGroupsResponse;
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
        MenuGroup menuGroup = request.toEntity();
        menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.from(menuGroup);
    }

    public MenuGroupsResponse list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupsResponse.from(menuGroups);
    }
}
