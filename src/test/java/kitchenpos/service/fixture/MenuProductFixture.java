package kitchenpos.service.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.service.dao.TestMenuDao;
import kitchenpos.service.dao.TestMenuProductDao;

public class MenuProductFixture {

    public static final Long 후라이드 = 1L;
    public static final Long 양념치킨 = 2L;
    public static final Long 반반치킨 = 3L;
    public static final Long 통구이 = 4L;
    public static final Long 간장치킨 = 5L;
    public static final Long 순살치킨 = 6L;

    private final TestMenuProductDao testMenuProductDao;

    private MenuProductFixture(TestMenuProductDao testMenuProductDao) {
        this.testMenuProductDao = testMenuProductDao;
    }

    public static MenuProductFixture createFixture(){
        MenuProductFixture menuProductFixture = new MenuProductFixture(new TestMenuProductDao());
        menuProductFixture.createMenuProduct();
        return menuProductFixture;
    }

    private void createMenuProduct() {
        saveMenuProduct(MenuFixture.후라이드치킨, ProductFixture.후라이드, 1L);
        saveMenuProduct(MenuFixture.양념치킨, ProductFixture.양념치킨, 1L);
        saveMenuProduct(MenuFixture.반반치킨, ProductFixture.반반치킨, 1L);
        saveMenuProduct(MenuFixture.통구이, ProductFixture.통구이, 1L);
        saveMenuProduct(MenuFixture.간장치킨, ProductFixture.간장치킨, 1L);
        saveMenuProduct(MenuFixture.순살치킨, ProductFixture.순살치킨, 1L);
    }

    public TestMenuProductDao getTestMenuProductDao() {
        return testMenuProductDao;
    }

    private MenuProduct saveMenuProduct(Long menuId, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return testMenuProductDao.save(menuProduct);
    }
}
