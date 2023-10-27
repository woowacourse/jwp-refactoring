package kitchenpos.menu_group.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu_group.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu_group.application.dto.MenuGroupResponse;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave(request.getName()));

        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
    }
}
