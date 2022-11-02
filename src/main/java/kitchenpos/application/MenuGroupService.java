package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupCreateResponse;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupListResponse;
import org.springframework.stereotype.Service;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

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
