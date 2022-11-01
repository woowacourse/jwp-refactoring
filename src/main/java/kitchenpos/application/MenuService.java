package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao, final ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final Menu menu) {
        validateMenuGroup(menu);
        validatePrice(menu);
        return menuDao.save(menu);
    }

    private void validateMenuGroup(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(final Menu menu) {
        final Products products = productDao.findAllByIdIn(menu.getMenuProductIds());
        menu.validatePrice(products);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
