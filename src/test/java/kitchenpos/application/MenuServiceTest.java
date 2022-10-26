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
import kitchenpos.application.dto.MenuCreateRequestDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
        MenuCreateRequestDto dto = new MenuCreateRequestDto("후라이드", BigDecimal.valueOf(19000), 1L,
                List.of(MENU_PRODUCT));
        Menu savedMenu = menuService.create(dto);

        //then
        assertThat(savedMenu.getName()).isEqualTo(MENU.getName());
        assertThat(savedMenu.getPrice()).isEqualTo(MENU.getPrice());
        assertThat(savedMenu.getMenuGroupId()).isNotNull();
    }

    @Test
        //TODO: 에러 핸들링 후, 수정하기
    void create_요청_가격이_0이거나_음수이면_에러를_반환한다() {
        assertThatThrownBy(() -> menuService.create(
                new MenuCreateRequestDto(
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
        MenuCreateRequestDto dto = new MenuCreateRequestDto("name", price, 1L,
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
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus).hasSize(1);
    }
}
