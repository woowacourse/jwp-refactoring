package kitchenpos.menu;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final ApplicationEventPublisher eventPublisher;

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuService(
            final ApplicationEventPublisher eventPublisher,
            final MenuDao menuDao,
            final MenuProductDao menuProductDao
    ) {
        this.eventPublisher = eventPublisher;
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        Menu menu = menuDto.toDomain();
        eventPublisher.publishEvent(menu);
        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }

        return MenuDto.of(savedMenu, savedMenuProducts);
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                        menuProductDao.findAllByMenuId(menu.getId())))
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }
}
