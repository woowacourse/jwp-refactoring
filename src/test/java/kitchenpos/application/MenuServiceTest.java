package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixtures.MenuFixture.*;
import static kitchenpos.fixtures.ProductFixture.아메리카노;
import static kitchenpos.fixtures.ProductFixture.쫀득쫀득치즈볼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @DisplayName("메뉴를 등록 할 수 있다.")
    @Test
    void testCreateMenu() {
        //given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(쫀득쫀득치즈볼()), Optional.of(아메리카노()));
        given(menuDao.save(any())).willReturn(치즈폭탄());
        given(menuProductDao.save(any())).willReturn(any());

        //when
        Menu actual = menuService.create(첫번째메뉴());

        //then
        assertThat(actual.getId()).isEqualTo(1L);
    }

    @DisplayName("메뉴의 가격은 존재해야 하고, 음수가 아니어야 한다.")
    @Test
    void testCreateMenuPriceNegative() {
        //given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(-1));

        //when, then
        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴에 포함된 메뉴 상품의 총 가격의 합보다 작아야 한다.")
    @Test
    void testCreateMenuTotalPrice() {
        //given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(쫀득쫀득치즈볼()), Optional.of(아메리카노()));

        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(100000000));
        menu.setMenuProducts(치즈폭탄().getMenuProducts());

        //when, then
        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 리스트를 조회할 수 있다.")
    @Test
    void testList() {
        //given
        given(menuDao.findAll()).willReturn(Arrays.asList(치즈폭탄(), 무많이뿌링클(), 둘이서알리오갈리오(), 아메리카노한잔()));

        //when
        List<Menu> actual = menuService.list();

        //then
        verify(menuDao, times(1)).findAll();
        assertThat(actual).hasSize(4);
    }
}
