package kitchenpos.menugroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupCreateResponse create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        final MenuGroup menuGroup = new MenuGroup(menuGroupCreateRequest.getName());
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        return MenuGroupCreateResponse.from(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupCreateResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        return menuGroups.stream()
                .map(MenuGroupCreateResponse::from)
                .collect(Collectors.toList());
    }
}
