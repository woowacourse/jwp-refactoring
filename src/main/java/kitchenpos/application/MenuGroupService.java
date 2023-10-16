package kitchenpos.application;

import kitchenpos.application.dto.request.CreateMenuGroupRequest;
import kitchenpos.application.dto.response.CreateMenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.mapper.MenuGroupMapper;
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
    public CreateMenuGroupResponse create(final CreateMenuGroupRequest menuGroup) {
        MenuGroup entity = MenuGroupMapper.toMenuGroup(menuGroup);
        MenuGroup save = menuGroupDao.save(entity);
        return CreateMenuGroupResponse.from(save);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
