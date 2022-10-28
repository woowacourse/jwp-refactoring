package kitchenpos.ui.apiservice;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;

@Service
public class MenuGroupApiService {

    private final MenuGroupService menuGroupService;

    public MenuGroupApiService(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    public MenuGroupResponse create(MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup(menuGroupRequest.getName()));
        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
