package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Repository.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.request.MenuGroupRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = request.toEntity();
        MenuGroup save = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.from(save);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
    }
}
