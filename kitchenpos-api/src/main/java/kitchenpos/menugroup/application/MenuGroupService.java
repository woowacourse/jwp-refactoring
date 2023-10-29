package kitchenpos.menugroup.application;

import kitchenpos.menugroup.application.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.response.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuGroupService {

    private MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupCreateRequest.getName());
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.from(savedMenuGroup);

    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
