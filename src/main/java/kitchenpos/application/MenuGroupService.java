package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.dto.menugroup.MenuGroupCreateResponse;
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
        final MenuGroup menuGroup = new MenuGroup(menuGroupCreateRequest.getName());
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        return MenuGroupCreateResponse.from(savedMenuGroup);
    }

    public List<MenuGroupCreateResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        return menuGroups.stream()
                .map(MenuGroupCreateResponse::from)
                .collect(Collectors.toList());
    }
}
