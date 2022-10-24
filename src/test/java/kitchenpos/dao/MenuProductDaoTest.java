package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuProductDaoTest {

    private MenuProductDao menuProductDao;

    @Autowired
    public MenuProductDaoTest(MenuProductDao menuProductDao) {
        this.menuProductDao = menuProductDao;
    }

    @Test
    void save() {
        // given
        MenuProduct product = new MenuProduct(1L, 1L, 2);
        // when
        MenuProduct savedProduct = menuProductDao.save(product);
        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void findById() {
        // given
        MenuProduct product = new MenuProduct(1L, 1L, 2);
        MenuProduct savedProduct = menuProductDao.save(product);

        // when
        Optional<MenuProduct> foundMenuProduct = menuProductDao.findById(savedProduct.getSeq());

        // then
        assertThat(foundMenuProduct).isPresent();
    }

    @Test
    void findAll() {
        // given
        MenuProduct product = new MenuProduct(1L, 1L, 2);
        menuProductDao.save(product);

        // when
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        // then
        int defaultSize = 6;
        assertThat(menuProducts).hasSize(1 + defaultSize);
    }

    @Test
    void findAllByMenuId() {
        // given
        long menuId = 1L;
        MenuProduct product = new MenuProduct(menuId, 1L, 2);
        menuProductDao.save(product);

        // when
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);

        // then
        int defaultSize = 1;
        assertThat(menuProducts).hasSize(1 + defaultSize);
    }
}
