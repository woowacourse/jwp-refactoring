package kitchenpos.dao;

import static kitchenpos.constants.DaoConstants.TEST_MENU_GROUP_NAME;
import static kitchenpos.constants.DaoConstants.TEST_MENU_NAME;
import static kitchenpos.constants.DaoConstants.TEST_MENU_PRICE;
import static kitchenpos.constants.DaoConstants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.DaoConstants.TEST_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosDaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    protected Long getCreatedMenuGroupId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(TEST_MENU_GROUP_NAME);

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Long savedMenuGroupId = savedMenuGroup.getId();
        assertThat(savedMenuGroupId).isNotNull();
        return savedMenuGroupId;
    }

    protected Long getCreatedMenuId() {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(getCreatedMenuGroupId());

        Menu savedMenu = menuDao.save(menu);

        Long savedMenuId = savedMenu.getId();
        assertThat(savedMenuId).isNotNull();
        return savedMenuId;
    }

    protected Long getCreatedProductId() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);

        Product savedProduct = productDao.save(product);

        Long savedProductId = savedProduct.getId();
        assertThat(savedProductId).isNotNull();
        return savedProductId;
    }
}
