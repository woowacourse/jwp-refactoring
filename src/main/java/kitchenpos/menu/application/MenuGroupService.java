package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
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
        MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.of(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.toList(menuGroupRepository.findAll());
    }
}
