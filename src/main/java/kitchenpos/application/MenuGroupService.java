package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse createMenuGroup(MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupCreateRequest.toMenuGroup();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.from(savedMenuGroup);
    }

    public List<MenuGroupResponse> listAllMenuGroups() {
        List<MenuGroup> allMenuGroups = menuGroupRepository.findAll();

        return allMenuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
