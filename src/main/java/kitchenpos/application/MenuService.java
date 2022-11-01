package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuDao menuDao;
    private final MenuValidator menuValidator;

    public MenuService(MenuDao menuDao, MenuValidator menuValidator) {
        this.menuDao = menuDao;
        this.menuValidator = menuValidator;
    }

    public Menu create(final MenuCreateRequest request) {
        Menu menu = Menu.create(request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                request.getMenuProducts(),
                menuValidator);
        return menuDao.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuDao.findAll();
    }
}
