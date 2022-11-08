package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.dto.MenuGroupCreateResponse;
import kitchenpos.menu.dto.MenuGroupFindResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupCreateResponse create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(menuGroupCreateRequest.getName()));
        return MenuGroupCreateResponse.from(menuGroup);
    }

    public List<MenuGroupFindResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(MenuGroupFindResponse::from)
                .collect(Collectors.toList());
    }
}
