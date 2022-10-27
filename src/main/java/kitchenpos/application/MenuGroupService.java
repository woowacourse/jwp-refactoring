package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(menuGroupRequest.getName()));
        return mapToMenuGroupResponse(menuGroup);
    }

    private MenuGroupResponse mapToMenuGroupResponse(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        return menuGroups.stream()
                .map(this::mapToMenuGroupResponse)
                .collect(Collectors.toList());
    }
}
