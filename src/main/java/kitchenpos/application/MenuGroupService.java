package kitchenpos.application;

import javax.transaction.Transactional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.dto.menugroup.MenuGroupsResponse;
import kitchenpos.repository.MenuGroupRepository;
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
