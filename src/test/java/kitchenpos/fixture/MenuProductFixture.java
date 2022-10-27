package kitchenpos.fixture;

import kitchenpos.dao.InMemoryMenuProductDao;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static final Long 맵슐랭 = 1L;
    public static final Long 허니콤보 = 2L;

    private final InMemoryMenuProductDao inMemoryMenuProductDao;

    public MenuProductFixture(final InMemoryMenuProductDao inMemoryMenuProductDao) {
        this.inMemoryMenuProductDao = inMemoryMenuProductDao;
    }

    public static MenuProductFixture setUp() {
        MenuProductFixture menuProductFixture = new MenuProductFixture(new InMemoryMenuProductDao());
        menuProductFixture.createMenuProducts();
        return menuProductFixture;
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
        return inMemoryMenuProductDao.save(menuProduct);
    }

    public InMemoryMenuProductDao getInMemoryMenuProductDao() {
        return inMemoryMenuProductDao;
    }
}
