package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupResponse create(String name) {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(name));
        return MenuGroupResponse.toResponse(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
            .map(MenuGroupResponse::toResponse)
            .collect(Collectors.toUnmodifiableList());
    }
}
