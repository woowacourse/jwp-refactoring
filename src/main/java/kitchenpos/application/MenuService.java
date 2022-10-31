package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_MENU_GROUP_EXCEPTION;

import java.util.List;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
    }

    public Menu create(final MenuRequest request) {
        validMenuGroup(request.getMenuGroupId());
        return menuDao.save(request.toMenu());
    }

    private void validMenuGroup(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_MENU_GROUP_EXCEPTION);
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuDao.findAll();
    }
}
