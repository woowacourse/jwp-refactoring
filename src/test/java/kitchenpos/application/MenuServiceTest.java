package kitchenpos.application;

import static kitchenpos.Fixture.MENU;
import static kitchenpos.Fixture.MENU_PRODUCT;
import static kitchenpos.Fixture.PRODUCT;
import static kitchenpos.RequestFixture.MENU_PRODUCT_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        //given
        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
        given(productDao.findById(anyLong()))
                .willReturn(Optional.of(PRODUCT));
        given(menuDao.save(any(Menu.class)))
                .willReturn(MENU);
        given(menuProductDao.save(any(MenuProduct.class)))
                .willReturn(MENU_PRODUCT);

        //when
        MenuCreateRequest dto = new MenuCreateRequest("후라이드", BigDecimal.valueOf(19000), 1L,
                List.of(MENU_PRODUCT));
        MenuResponse savedMenu = menuService.create(dto);

        //then
        assertThat(savedMenu.getName()).isEqualTo(MENU.getName());
        assertThat(savedMenu.getPrice()).isEqualTo(MENU.getPrice());
        assertThat(savedMenu.getMenuGroupId()).isNotNull();
    }

    @Test
        //TODO: 에러 핸들링 후, 수정하기
    void create_요청_가격이_0이거나_음수이면_에러를_반환한다() {
        assertThatThrownBy(() -> menuService.create(
                new MenuCreateRequest(
                        "name", BigDecimal.valueOf(-100),
                        1L, List.of(MENU_PRODUCT_REQUEST)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메뉴가격이_상품_각각의_가격보다_비싸면_에러를_반환한다() {
        //given
        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
        given(productDao.findById(anyLong()))
                .willReturn(Optional.of(new Product("product", BigDecimal.valueOf(10000))));

        //when, then
        BigDecimal price = BigDecimal.valueOf(30000);
        MenuCreateRequest dto = new MenuCreateRequest("name", price, 1L,
                List.of(MENU_PRODUCT_REQUEST));

        assertThatThrownBy(() -> menuService.create(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() {
        //given
        given(menuDao.findAll()).willReturn(List.of(MENU));

        //when
        List<MenuResponse> menus = menuService.list();

        //then
        assertThat(menus).hasSize(1);
    }
}
