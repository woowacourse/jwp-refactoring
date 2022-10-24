package kitchenpos.application;

import static kitchenpos.Fixture.MENU;
import static kitchenpos.Fixture.MENU_PRODUCT;
import static kitchenpos.Fixture.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
        Menu menu = menuService.create(MENU);

        //then
        assertThat(menu.getName()).isEqualTo("후라이드+후라이드");
        assertThat(menu.getPrice()).isEqualTo( BigDecimal.valueOf(19000));
        assertThat(menu.getMenuGroupId()).isNotNull();
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() {
        //given
        given(menuDao.findAll()).willReturn(List.of(MENU));

        //when
        List<Menu> menues = menuService.list();

        //then
        assertThat(menues).hasSize(1);
    }
}
