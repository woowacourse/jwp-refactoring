package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.Fixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Test
    void create() {
        // given
        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
        given(productDao.findById(1L))
                .willReturn(Optional.of(Fixtures.product(1, "상품1", 1000)));
        given(productDao.findById(2L))
                .willReturn(Optional.of(Fixtures.product(2, "상품2", 2000)));
        given(menuDao.save(any(Menu.class)))
                .willReturn(Fixtures.menu(1, "메뉴", 5000, 1, null));
        given(menuProductDao.save(any(MenuProduct.class)))
                .willReturn(
                        Fixtures.menuProduct(1, 1, 1, 1),
                        Fixtures.menuProduct(2, 1, 2, 2)
                );

        // when
        Menu menu = Fixtures.menu(1, "메뉴", 5000, 1,
                Arrays.asList(
                        Fixtures.menuProduct(1, 1, 1, 1),
                        Fixtures.menuProduct(2, 1, 2, 2)
                ));
        Menu createdMenu = menuService.create(menu);

        // then
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(createdMenu.getName()).isEqualTo(menu.getName());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        List<MenuProduct> menuProducts = createdMenu.getMenuProducts();
        assertThat(menuProducts).hasSize(2);
    }

    @Test
    void list() {
        // given
        given(menuDao.findAll())
                .willReturn(Collections.singletonList(
                        Fixtures.menu(1, "메뉴1", 1000, 1, null)));
        given(menuProductDao.findAllByMenuId(anyLong()))
                .willReturn(Collections.singletonList(
                        Fixtures.menuProduct(1, 1, 1, 1)));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
        Menu menu = menus.get(0);
        assertThat(menu.getId()).isEqualTo(1);
        assertThat(menu.getName()).isEqualTo("메뉴1");
        assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(menu.getMenuGroupId()).isEqualTo(1);
        assertThat(menu.getMenuProducts()).hasSize(1);

        MenuProduct menuProduct = menu.getMenuProducts().get(0);
        assertThat(menuProduct.getSeq()).isEqualTo(1);
        assertThat(menuProduct.getProductId()).isEqualTo(1);
        assertThat(menuProduct.getMenuId()).isEqualTo(1);
        assertThat(menuProduct.getQuantity()).isEqualTo(1);
    }
}