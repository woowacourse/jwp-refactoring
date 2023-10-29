package kitchenpos.order.application;

import kitchenpos.order.domain.MenuGroup;
import kitchenpos.order.domain.repository.MenuGroupRepository;
import kitchenpos.order.ui.request.MenuGroupRequest;
import kitchenpos.order.ui.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        final MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.from(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        final List<MenuGroup> allMenuGroup = menuGroupRepository.findAll();
        return allMenuGroup.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
