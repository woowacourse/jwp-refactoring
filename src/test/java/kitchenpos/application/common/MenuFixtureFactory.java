package kitchenpos.application.common;

import kitchenpos.TestObjectFactory;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class MenuFixtureFactory {
    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    protected Menu createMenuToSave(String menuGroupName, String productName, int productPrice) {
        MenuGroup savedMenuGroup = menuGroupDao.save(TestObjectFactory.createMenuGroupDto(menuGroupName));
        Product savedProduct = productDao.save(TestObjectFactory.createProductDto(productName, productPrice));

        List<MenuProduct> menuProducts = Arrays.asList(TestObjectFactory.createMenuProduct(savedProduct.getId(), 2));
        return TestObjectFactory.createMenuDto(productName + "+" + productName, productPrice * 2, savedMenuGroup.getId(), menuProducts);
    }
}
