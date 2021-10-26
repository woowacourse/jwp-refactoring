package kitchenpos.integration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixtureMaker {
    @Autowired
    private JdbcTemplateMenuDao menuDao;

    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @Autowired
    private JdbcTemplateProductDao productDao;

    public List<MenuProduct> createMenuProducts() {
        // 상품 생성
        Product product1 = productDao.save(new Product("상품1", new BigDecimal(16_000)));
        Product product2 = productDao.save(new Product("상품2", new BigDecimal(17_000)));
        Product product3 = productDao.save(new Product("상품3", new BigDecimal(18_000)));

        // 메뉴 그룹 생성
        MenuGroup menuGroup = createMenuGroup();

        // 메뉴 생성
        Menu menu1 = menuDao.save(new Menu("메뉴1", new BigDecimal(16_000), menuGroup.getId()));
        Menu menu2 = menuDao.save(new Menu("메뉴2", new BigDecimal(17_000), menuGroup.getId()));
        Menu menu3 = menuDao.save(new Menu("메뉴3", new BigDecimal(18_000), menuGroup.getId()));

        // 메뉴를 구성하는 상품 생성
        MenuProduct menuProduct1 = new MenuProduct(menu1.getId(), product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(menu2.getId(), product2.getId(), 2);
        MenuProduct menuProduct3 = new MenuProduct(menu3.getId(), product3.getId(), 3);

        return Arrays.asList(
            menuProduct1, menuProduct2, menuProduct3
        );
    }

    public MenuGroup createMenuGroup() {
        return menuGroupDao.save(new MenuGroup("메뉴 그룹"));
    }
}
