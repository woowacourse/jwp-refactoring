package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    protected ProductService productService;
    @Autowired
    protected MenuService menuService;

    @Autowired
    protected MenuGroupDao menuGroupDao;
    @Autowired
    protected ProductDao productDao;

    protected Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    protected Product createAndSaveProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return productDao.save(product);
    }

    protected MenuProduct createMenuProduct(long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        menuGroup = menuGroupDao.save(menuGroup);

        return menuGroupDao.save(menuGroup);
    }

    protected Menu createMenu(String name, BigDecimal price, long menuGroupId, MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(new ArrayList<MenuProduct>() {{
            add(menuProduct);
        }});

        return menu;
    }
}
