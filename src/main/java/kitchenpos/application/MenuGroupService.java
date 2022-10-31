package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
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
    public MenuGroupResponse create(MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupDao.save(generateMenuGroup(menuGroupCreateRequest));
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
                .map(each -> new MenuGroupResponse(each.getId(), each.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    private MenuGroup generateMenuGroup(MenuGroupCreateRequest menuGroupCreateRequest) {
        validateName(menuGroupCreateRequest);
        return new MenuGroup(menuGroupCreateRequest.getName());
    }

    private void validateName(MenuGroupCreateRequest menuGroupCreateRequest) {
        if (menuGroupCreateRequest.getName() == null) {
            throw new IllegalArgumentException();
        }
    }
}
