package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.application.dto.menugroup.MenuGroupCreateResponse;
import kitchenpos.application.dto.menugroup.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupCreateResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return MenuGroupCreateResponse.of(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(toList());
    }
}
