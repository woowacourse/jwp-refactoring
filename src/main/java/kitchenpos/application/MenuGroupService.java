package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(request.getMenuGroupName());
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return MenuGroupResponse.of(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> allMenuGroup = menuGroupDao.findAll();
        return MenuGroupResponse.of(allMenuGroup);
    }
}
