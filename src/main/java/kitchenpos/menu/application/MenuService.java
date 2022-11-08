package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.application.validator.MenuValidator;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final MenuValidator menuValidator;
    private final MenuCreateEventPublisher menuCreateEventPublisher;

    public MenuService(final MenuDao menuDao, final MenuValidator menuValidator,
        final MenuCreateEventPublisher menuCreateEventPublisher) {
        this.menuDao = menuDao;
        this.menuValidator = menuValidator;
        this.menuCreateEventPublisher = menuCreateEventPublisher;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Menu menu = request.toEntity();
        menuValidator.validate(menu);
        Menu savedMenu = menuDao.save(menu);
        menuCreateEventPublisher.publish(savedMenu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuDao.findAll());
    }
}
