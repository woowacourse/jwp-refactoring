package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.entity.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.ui.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.menu.ui.dto.menugroup.MenuGroupCreateResponse;
import kitchenpos.menu.ui.dto.menugroup.MenuGroupListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupCreateResponse create(MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupCreateRequest.getName());
        menuGroupRepository.save(menuGroup);
        return new MenuGroupCreateResponse(menuGroup.getId(), menuGroup.getName());
    }

    public List<MenuGroupListResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return menuGroups.stream()
                .map(menuGroup -> new MenuGroupListResponse(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
    }

    public MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
