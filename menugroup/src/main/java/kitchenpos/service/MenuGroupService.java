package kitchenpos.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.CreateMenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(CreateMenuGroupRequest request) {
        MenuGroup menuGroup = new MenuGroup(request.getName());
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.from(savedMenuGroup);
    }

    public List<MenuGroupResponse> findAll() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public void validateMenuGroupExists(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(MenuGroupNotFoundException::new);
    }
}
