package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static kitchenpos.testutils.TestDomainBuilder.menuBuilder;
import static kitchenpos.testutils.TestDomainBuilder.menuProductBuilder;
import static kitchenpos.testutils.TestDomainBuilder.productBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@MockitoSettings
class MenuServiceTest {

    private static final Long NON_EXISTENT_ID = 987654321L;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    private Product friedChickenProduct, seasoningChickenProduct;
    private Long doubleQuantityMenuGroupId;

    @BeforeEach
    void setUp() {
        friedChickenProduct = productBuilder()
                .id(1L)
                .name("후라이드")
                .price(BigDecimal.valueOf(16000))
                .build();
        seasoningChickenProduct = productBuilder()
                .id(2L)
                .name("양념치킨")
                .price(BigDecimal.valueOf(16000))
                .build();
        doubleQuantityMenuGroupId = 1L;
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        // given
        MenuProduct newMenuProduct1 = menuProductBuilder()
                .productId(friedChickenProduct.getId())
                .quantity(1)
                .build();
        MenuProduct newMenuProduct2 = menuProductBuilder()
                .productId(seasoningChickenProduct.getId())
                .quantity(1)
                .build();

        Menu newMenu = menuBuilder()
                .id(1L)
                .name("후라이드+양념")
                .price(BigDecimal.valueOf(19000))
                .menuGroupId(doubleQuantityMenuGroupId)
                .menuProducts(Arrays.asList(newMenuProduct1, newMenuProduct2))
                .build();

        given(menuGroupDao.existsById(newMenu.getMenuGroupId()))
                .willReturn(true);
        given(productDao.findById(anyLong())).willReturn(
                Optional.ofNullable(friedChickenProduct),
                Optional.ofNullable(seasoningChickenProduct)
        );
        given(menuDao.save(newMenu)).willReturn(newMenu);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(
                newMenuProduct1, newMenuProduct2
        );

        // when
        Menu menu = menuService.create(newMenu);

        // then
        assertThat(menu.getMenuProducts()).isNotNull();
        assertThat(menu.getMenuProducts()).extracting(MenuProduct::getMenuId).doesNotContainNull();

        then(menuGroupDao).should(times(1)).existsById(doubleQuantityMenuGroupId);
        then(productDao).should(times(2)).findById(anyLong());
        then(menuDao).should(times(1)).save(newMenu);
        then(menuProductDao).should(times(2)).save(any(MenuProduct.class));
    }

    @DisplayName("메뉴 생성에 실패한다 - 가격은 null 이거나 음수일 수 없다.")
    @MethodSource("invalidPriceSource")
    @ParameterizedTest
    void createWithInvalidPrice(BigDecimal price) {
        // given
        MenuProduct newMenuProduct = menuProductBuilder()
                .productId(friedChickenProduct.getId())
                .quantity(2)
                .build();
        Menu newMenu = menuBuilder()
                .name("후라이드+후라이드")
                .price(price)
                .menuGroupId(doubleQuantityMenuGroupId)
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);

        then(menuDao).should(never()).save(any());
        then(menuProductDao).should(never()).save(any());
    }

    private static Stream<BigDecimal> invalidPriceSource() {
        return Stream.of(
                null,
                BigDecimal.valueOf(-1),
                BigDecimal.valueOf(-10000)
        );
    }

    @DisplayName("메뉴 생성에 실패한다 - 추가할 메뉴의 메뉴 그룹 아이디는 존재해야 한다.")
    @Test
    void createWithNonexistentMenuGroupId() {
        // given
        MenuProduct newMenuProduct = menuProductBuilder()
                .productId(friedChickenProduct.getId())
                .quantity(2)
                .build();
        Menu newMenu = menuBuilder()
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19000))
                .menuGroupId(NON_EXISTENT_ID)
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        given(menuGroupDao.existsById(NON_EXISTENT_ID))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);

        then(menuDao).should(never()).save(any());
        then(menuProductDao).should(never()).save(any());
    }

    @DisplayName("메뉴 생성에 실패한다 - 메뉴 상품 리스트가 가지고 있는 상품은 모두 존재해야 한다.")
    @Test
    void createWithNonexistentProduct() {
        // given
        MenuProduct newMenuProduct = menuProductBuilder()
                .productId(NON_EXISTENT_ID)
                .quantity(2)
                .build();
        Menu newMenu = menuBuilder()
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19000))
                .menuGroupId(doubleQuantityMenuGroupId)
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        given(menuGroupDao.existsById(doubleQuantityMenuGroupId))
                .willReturn(true);
        given(productDao.findById(NON_EXISTENT_ID))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);

        then(menuDao).should(never()).save(any());
        then(menuProductDao).should(never()).save(any());
    }

    @DisplayName("메뉴 생성에 실패한다 - 메뉴 상품 리스트의 ∑(상품가격 * 수량)이 추가할 메뉴의 가격보다 작거나 같아야 한다.")
    @Test
    void createWithInvalidTotalProductSum() {
        // given
        MenuProduct newMenuProduct = menuProductBuilder()
                .productId(friedChickenProduct.getId())
                .quantity(2)
                .build();
        Menu newMenu = menuBuilder()
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(32001))
                .menuGroupId(doubleQuantityMenuGroupId)
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        given(menuGroupDao.existsById(doubleQuantityMenuGroupId))
                .willReturn(true);
        given(productDao.findById(newMenuProduct.getProductId()))
                .willReturn(Optional.ofNullable(friedChickenProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);

        then(menuDao).should(never()).save(any());
        then(menuProductDao).should(never()).save(any());
    }

    @DisplayName("전체 메뉴의 리스트를 가져온다.")
    @Test
    void list() {
        // given
        Menu menu1 = menuBuilder()
                .id(1L)
                .build();
        Menu menu2 = menuBuilder()
                .id(2L)
                .build();

        MenuProduct menuProduct = menuProductBuilder()
                .build();

        given(menuDao.findAll()).willReturn(
                Arrays.asList(menu1, menu2)
        );
        given(menuProductDao.findAllByMenuId(anyLong()))
                .willReturn(Collections.singletonList(menuProduct));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).extracting(Menu::getMenuProducts).isNotEmpty();

        then(menuDao).should(times(1)).findAll();
        then(menuProductDao).should(times(2)).findAllByMenuId(anyLong());
    }
}
