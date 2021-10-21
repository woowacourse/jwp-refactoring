package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Collections;

public class DomainDaoTest extends DaoTest{

    private MenuDao menuDao;

    protected long SAVE_MENU() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(1L);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        // when - then
        Menu savedMenu = menuDao.save(menu);
        return savedMenu.getId();
    }
}
