package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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
import static kitchenpos.testutils.TestDomainBuilder.menuGroupBuilder;
import static kitchenpos.testutils.TestDomainBuilder.menuProductBuilder;
import static kitchenpos.testutils.TestDomainBuilder.productBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private MenuGroup doubleQuantityMenuGroup, singleQuantityMenuGroup;
    private Menu friedChickenMenu, seasoningChickenMenu;
    private MenuProduct singleFriedChickenMenuProduct, singleSeasoningChickenProduct;

    @BeforeEach
    void setUp() {
        initializeData();
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        // given
        MenuProduct newMenuProduct = menuProductBuilder()
                .productId(friedChickenProduct.getId())
                .quantity(2)
                .build();
        Menu newMenu = menuBuilder()
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19000))
                .menuGroupId(doubleQuantityMenuGroup.getId())
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        Menu savedMenu = menuBuilder()
                .id(3L)
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19000))
                .menuGroupId(doubleQuantityMenuGroup.getId())
                .build();
        Menu composedMenu = menuBuilder()
                .id(3L)
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19000))
                .menuGroupId(doubleQuantityMenuGroup.getId())
                .build();
        MenuProduct expectedMenuProduct = menuProductBuilder()
                .seq(3L)
                .menuId(composedMenu.getId())
                .productId(friedChickenProduct.getId())
                .quantity(2)
                .build();
        composedMenu.setMenuProducts(Collections.singletonList(expectedMenuProduct));

        given(
                menuGroupDao.existsById(newMenu.getMenuGroupId())
        ).willReturn(true);
        given(
                productDao.findById(newMenuProduct.getProductId())
        ).willReturn(Optional.ofNullable(friedChickenProduct));
        given(
                menuDao.save(newMenu)
        ).willReturn(savedMenu);
        given(
                menuProductDao.save(newMenuProduct)
        ).willReturn(expectedMenuProduct);

        // when
        Menu menu = menuService.create(newMenu);

        // then
        assertThat(menu).usingRecursiveComparison().isEqualTo(composedMenu);

        then(menuGroupDao).should(times(1)).existsById(newMenu.getMenuGroupId());
        then(productDao).should(times(1)).findById(newMenuProduct.getProductId());
        then(menuDao).should(times(1)).save(newMenu);
        then(menuProductDao).should(times(1)).save(newMenuProduct);
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
                .menuGroupId(doubleQuantityMenuGroup.getId())
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(never()).existsById(newMenu.getMenuGroupId());
        then(productDao).should(never()).findById(newMenuProduct.getProductId());
        then(menuDao).should(never()).save(newMenu);
        then(menuProductDao).should(never()).save(newMenuProduct);
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

        given(
                menuGroupDao.existsById(NON_EXISTENT_ID)
        ).willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(times(1)).existsById(newMenu.getMenuGroupId());

        then(productDao).should(never()).findById(newMenuProduct.getProductId());
        then(menuDao).should(never()).save(newMenu);
        then(menuProductDao).should(never()).save(newMenuProduct);
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
                .menuGroupId(doubleQuantityMenuGroup.getId())
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        given(
                menuGroupDao.existsById(newMenu.getMenuGroupId())
        ).willReturn(true);
        given(
                productDao.findById(newMenuProduct.getProductId())
        ).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(times(1)).existsById(newMenu.getMenuGroupId());
        then(productDao).should(times(1)).findById(NON_EXISTENT_ID);

        then(menuDao).should(never()).save(newMenu);
        then(menuProductDao).should(never()).save(newMenuProduct);
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
                .menuGroupId(doubleQuantityMenuGroup.getId())
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        given(
                menuGroupDao.existsById(newMenu.getMenuGroupId())
        ).willReturn(true);
        given(
                productDao.findById(newMenuProduct.getProductId())
        ).willReturn(Optional.ofNullable(friedChickenProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(times(1)).existsById(newMenu.getMenuGroupId());
        then(productDao).should(times(1)).findById(newMenuProduct.getProductId());

        then(menuDao).should(never()).save(newMenu);
        then(menuProductDao).should(never()).save(newMenuProduct);
    }

    @DisplayName("전체 메뉴의 리스트를 가져온다.")
    @Test
    void list() {
        // given
        Menu savedFriedChickenMenu = menuBuilder()
                .id(friedChickenMenu.getId())
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(singleQuantityMenuGroup.getId())
                .build();

        Menu savedSeasoningChickenMenu = menuBuilder()
                .id(seasoningChickenMenu.getId())
                .name("양념치킨")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(singleQuantityMenuGroup.getId())
                .build();

        List<Menu> savedMenus = Arrays.asList(savedFriedChickenMenu, savedSeasoningChickenMenu);

        given(menuDao.findAll()).willReturn(savedMenus);
        given(
                menuProductDao.findAllByMenuId(friedChickenMenu.getId())
        ).willReturn(Collections.singletonList(singleFriedChickenMenuProduct));
        given(
                menuProductDao.findAllByMenuId(seasoningChickenMenu.getId())
        ).willReturn(Collections.singletonList(singleSeasoningChickenProduct));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus)
                .usingElementComparatorIgnoringFields("price")
                .containsExactly(friedChickenMenu, seasoningChickenMenu);
    }

    private void initializeData() {
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

        doubleQuantityMenuGroup = menuGroupBuilder()
                .id(1L)
                .name("두마리메뉴")
                .build();
        singleQuantityMenuGroup = menuGroupBuilder()
                .id(2L)
                .name("한마리메뉴")
                .build();

        friedChickenMenu = menuBuilder()
                .id(1L)
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(singleQuantityMenuGroup.getId())
                .build();
        seasoningChickenMenu = menuBuilder()
                .id(2L)
                .name("양념치킨")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(singleQuantityMenuGroup.getId())
                .build();

        singleFriedChickenMenuProduct = menuProductBuilder()
                .seq(1L)
                .menuId(friedChickenMenu.getId())
                .productId(friedChickenProduct.getId())
                .quantity(1)
                .build();
        singleSeasoningChickenProduct = menuProductBuilder()
                .seq(2L)
                .menuId(seasoningChickenMenu.getId())
                .productId(seasoningChickenProduct.getId())
                .quantity(1)
                .build();

        friedChickenMenu.setMenuProducts(Collections.singletonList(singleFriedChickenMenuProduct));
        seasoningChickenMenu.setMenuProducts(Collections.singletonList(singleSeasoningChickenProduct));
    }
}
