package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest request) {
        MenuGroup menuGroup = new MenuGroup(request.getName());
        return MenuGroupResponse.from(menuGroupDao.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
                .map(MenuGroupResponse::from)
                .collect(toList());
    }
}
