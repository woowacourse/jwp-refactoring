package kitchenpos.application;

import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        final MenuGroup menuGroup = convertToMenuGroup(request);
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return convertToMenuGroupResponse(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return convertToMenuGroupResponses(menuGroups);
    }

    private MenuGroup convertToMenuGroup(final MenuGroupRequest request) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(request.getName());
        return menuGroup;
    }

    private MenuGroupResponse convertToMenuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    private List<MenuGroupResponse> convertToMenuGroupResponses(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()))
            .collect(Collectors.toUnmodifiableList());
    }
}
