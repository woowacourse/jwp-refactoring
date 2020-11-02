package kitchenpos.dao;

import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT_FIXTURE_1;
import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT_FIXTURE_2;
import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT_FIXTURE_3;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuProductDaoTest {

    @Autowired
    private MenuProductDao menuProductDao;

    @Test
    void save() {
        MenuProduct menuProduct = MENU_PRODUCT_FIXTURE_1;

        MenuProduct persistMenuProduct = menuProductDao.save(menuProduct);

        assertThat(menuProduct).isEqualToIgnoringGivenFields(persistMenuProduct, "seq");
    }

    @Test
    void findById() {
        MenuProduct persistMenuProduct = menuProductDao.save(MENU_PRODUCT_FIXTURE_1);

        MenuProduct findMenuProduct = menuProductDao.findById(persistMenuProduct.getSeq()).get();

        assertThat(findMenuProduct).isEqualToComparingFieldByField(persistMenuProduct);
    }

    @Test
    void findAll() {
        menuProductDao.save(MENU_PRODUCT_FIXTURE_1);
        menuProductDao.save(MENU_PRODUCT_FIXTURE_2);

        List<MenuProduct> menuProducts = menuProductDao.findAll();
        List<Long> menuIds = menuProducts.stream()
            .map(MenuProduct::getMenuId)
            .collect(Collectors.toList());
        List<Long> productIds = menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());

        assertThat(menuIds).contains(MENU_PRODUCT_FIXTURE_1.getMenuId(), MENU_PRODUCT_FIXTURE_2.getMenuId());
        assertThat(productIds).contains(MENU_PRODUCT_FIXTURE_1.getProductId(), MENU_PRODUCT_FIXTURE_2.getProductId());
    }

    @Test
    void findAllByMenuId() {
        menuProductDao.save(MENU_PRODUCT_FIXTURE_1);
        menuProductDao.save(MENU_PRODUCT_FIXTURE_2);
        menuProductDao.save(MENU_PRODUCT_FIXTURE_3);

        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(1L);
        List<Long> menuIds = menuProducts.stream()
            .map(MenuProduct::getMenuId)
            .collect(Collectors.toList());
        List<Long> productIds = menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());

        assertThat(menuIds).contains(MENU_PRODUCT_FIXTURE_1.getMenuId(), MENU_PRODUCT_FIXTURE_3.getMenuId());
        assertThat(productIds).contains(MENU_PRODUCT_FIXTURE_1.getProductId(), MENU_PRODUCT_FIXTURE_3.getProductId());
    }
}
