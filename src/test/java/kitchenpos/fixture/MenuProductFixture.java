package kitchenpos.fixture;

import kitchenpos.dao.InMemoryMenuProductDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static final Long 맵슐랭 = 1L;
    public static final Long 허니콤보 = 2L;

    private final MenuProductDao menuProductDao;

    public MenuProductFixture(final MenuProductDao menuProductDao) {
        this.menuProductDao = menuProductDao;
    }

    public static MenuProductFixture setUp() {
        MenuProductFixture menuProductFixture = new MenuProductFixture(new InMemoryMenuProductDao());
        menuProductFixture.createMenuProducts();
        return menuProductFixture;
    }

    public static MenuProduct createMenuProduct(final Long productId) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    private void createMenuProducts() {
        saveMenuProduct(MenuFixture.맵슐랭, ProductFixture.맵슐랭, 1L);
        saveMenuProduct(MenuFixture.허니콤보, ProductFixture.허니콤보, 1L);
    }

    private MenuProduct saveMenuProduct(final Long menuId, final Long productId, final Long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProductDao.save(menuProduct);
    }

    public MenuProductDao getMenuProductDao() {
        return menuProductDao;
    }
}
