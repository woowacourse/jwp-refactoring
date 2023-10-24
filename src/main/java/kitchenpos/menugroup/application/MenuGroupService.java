package kitchenpos.menugroup.application;

import javax.transaction.Transactional;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.application.dto.MenuGroupsResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(request.getName()));
        return MenuGroupResponse.from(savedMenuGroup);
    }

    public MenuGroupsResponse list() {
        return MenuGroupsResponse.from(menuGroupRepository.findAll());
    }
}
