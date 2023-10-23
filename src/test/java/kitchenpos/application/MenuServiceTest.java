package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.support.domain.MenuTestSupport;
import kitchenpos.application.support.domain.ProductTestSupport;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuDao menuDao;
    @Mock
    MenuGroupDao menuGroupDao;
    @Mock
    MenuProductDao menuProductDao;
    @Mock
    ProductDao productDao;
    @InjectMocks
    MenuService target;

    @DisplayName("메뉴를 생성하면 DB에 저장해서 반환한다.")
    @Test
    void create() {
        //given
        final MenuTestSupport.Builder builder = MenuTestSupport.builder();
        final Menu menu = builder.build();
        final MenuCreateRequest request = builder.buildToMenuCreateRequest();
        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        for (MenuProduct menuProduct : menuProducts) {
            final Product product = ProductTestSupport.builder().id(menuProduct.getProductId()).build();
            given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        }
        given(menuDao.save(any(Menu.class))).willReturn(menu);
        //when

        //then
        Assertions.assertDoesNotThrow(() -> target.create(request));
    }

    @DisplayName("메뉴의 가격이 음수이면 예외처리한다.")
    @Test
    void create_fail_price_minus() {
        //given
        final BigDecimal price = new BigDecimal("-1");
        final MenuCreateRequest request = MenuTestSupport.builder().price(price).buildToMenuCreateRequest();
        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 메뉴 그룹에 포함되지 않으면 예외처리한다.")
    @Test
    void create_fail_not_in_menuGroup() {
        //given
        final MenuCreateRequest request = MenuTestSupport.builder().menuGroupId(null).buildToMenuCreateRequest();

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력받은 메뉴의 가격은 메뉴 상품들의 가격보다 비싸면 안된다.")
    @Test
    void create_fail_price_greaterThan_product_price() {
        //given
        final BigDecimal price = new BigDecimal(Long.MAX_VALUE);
        final MenuTestSupport.Builder builder = MenuTestSupport.builder().price(price);
        final Menu menu = builder.build();
        final MenuCreateRequest request = builder.buildToMenuCreateRequest();
        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        for (MenuProduct menuProduct : menuProducts) {
            final Product product = ProductTestSupport.builder().id(menuProduct.getProductId()).build();
            given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        }
        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 메뉴를 조회한다.")
    @Test
    void list() {
        //given
        final Menu menu1 = MenuTestSupport.builder().build();
        final Menu menu2 = MenuTestSupport.builder().build();

        final List<Menu> menus = List.of(menu1, menu2);
        given(menuDao.findAll()).willReturn(menus);
        for (Menu menu : menus) {
            given(menuProductDao.findAllByMenuId(menu.getId())).willReturn(menu.getMenuProducts());
        }

        //when
        final List<Menu> result = target.list();

        //then
        assertThat(result)
                .extracting(Menu::getName)
                .contains(menu1.getName(), menu2.getName());
    }
}
