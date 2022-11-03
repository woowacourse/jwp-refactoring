package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.application.validator.MenuValidator;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final MenuValidator menuValidator;

    public MenuService(final MenuDao menuDao, final MenuValidator menuValidator) {
        this.menuDao = menuDao;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(),
            request.getMenuProducts());
        menuValidator.validate(menu);
        return MenuResponse.from(menuDao.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.fromAll(menuDao.findAll());
    }
}
