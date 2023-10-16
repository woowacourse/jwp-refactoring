package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Test
    void Menu를_생성할_수_있다() {
        //given
        final Product product = productDao.save(new Product("디노 탕후루", new BigDecimal(4000)));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("탕후루"));
        final MenuProduct menuProduct = new MenuProduct(null, product.getId(), 2);
        final Menu menu = new Menu("디노 세트", new BigDecimal(8000),
                menuGroup.getId(), List.of(menuProduct));

        //when
        final Menu saveMenu = menuService.create(menu);

        //then
        assertThat(saveMenu.getId()).isNotNull();
    }

    @Test
    void price가_null이면_예외가_발생한다() {
        //given
        final Menu menu = new Menu("디노 찜구이", null, 1L, null);

        //when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_0원보다_작으면_예외가_발생한다() {
        //given
        final Menu menu = new Menu("디노 찜구이", new BigDecimal(-1), 1L, null);

        //when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menuGroup이_존재하지_않으면_예외가_발생한다() {
        //given
        final Menu menu = new Menu("디노 찜구이", new BigDecimal(20000), 987654321L, null);

        //when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품의_가격_합계를_초과하면_예외가_발생한다() {
        //given
        final Product product = productDao.save(new Product("디노 탕후루", new BigDecimal(4000)));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("탕후루"));
        final MenuProduct menuProduct = new MenuProduct(null, product.getId(), 2);

        //when
        final Menu menu = new Menu("디노 세트", new BigDecimal(9000),
                menuGroup.getId(), List.of(menuProduct));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Menu를_조회할_수_있다() {
        //given
        final Product product = productDao.save(new Product("디노 탕후루", new BigDecimal(4000)));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("탕후루"));
        final MenuProduct menuProduct = new MenuProduct(null, product.getId(), 2);
        final Menu menu = new Menu("디노 세트", new BigDecimal(8000),
                menuGroup.getId(), List.of(menuProduct));
        menuDao.save(menu);

        //when
        final List<Menu> list = menuService.list();

        //then
        assertThat(list).hasSize(1);
    }
}
