package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuDao;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupService menuGroupService;
    private final MenuProductService menuProductService;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupService menuGroupService,
            final MenuProductService menuProductService
    ) {
        this.menuDao = menuDao;
        this.menuGroupService = menuGroupService;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final BigDecimal price = menuProductService.calculateSavedPrice(menuRequest.getMenuProducts());
        final Menu menu = menuRequest.toEntity(menuGroup, price);

        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> menuProducts = menuProductService.saveAll(savedMenu, menuRequest.getMenuProducts());
        savedMenu.addMenuProducts(menuProducts);
        return savedMenu;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    public Menu findById(long menuId) {
        return menuDao.findById(menuId)
                      .orElseThrow(() -> new IllegalArgumentException("MenuId에 해당하는 메뉴가 존재하지 않습니다."));
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuDao.countByIdIn(menuIds);
    }
}
