package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.CreateMenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(CreateMenuGroupRequest request) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(request.getName());
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.from(savedMenuGroup);
    }

    public List<MenuGroupResponse> findAll() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
