package kitchenpos.application.menugroup;

import javax.transaction.Transactional;
import kitchenpos.application.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.application.menugroup.dto.MenuGroupResponse;
import kitchenpos.application.menugroup.dto.MenuGroupsResponse;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
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
