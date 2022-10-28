package kitchenpos.application;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuDao menuDao;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(MenuRepository menuRepository, MenuDao menuDao, MenuGroupRepository menuGroupRepository,
                       MenuProductDao menuProductDao, ProductDao productDao) {
        this.menuRepository = menuRepository;
        this.menuDao = menuDao;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                menuRequest.getMenuProducts());

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menuRequest.getMenuProducts()) {
            menuProduct.setMenu(menuRepository.findById(menuId)
                    .orElseThrow(IllegalArgumentException::new));
            //menuProduct.setMenuId(menuId);

            final Product product = productDao.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);

            final MenuProduct tempMenuProduct = menuProductDao.save(menuProduct);
            tempMenuProduct.setProduct(product);
            savedMenuProducts.add(tempMenuProduct);
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
