package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
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
    public MenuGroup create(MenuGroupCreateRequest menuGroupCreateRequest) {
        return menuGroupDao.save(generateMenuGroup(menuGroupCreateRequest));
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
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
