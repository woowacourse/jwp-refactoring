package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.dao.MenuGroupDao;
import kitchenpos.menu.response.MenuGroupResponse;
import kitchenpos.menu.ui.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroup request) {
        return MenuGroupResponse.from(menuGroupDao.save(request));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
