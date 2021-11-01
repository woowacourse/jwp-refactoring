package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.factory.MenuFactory;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.ProductFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
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

    @DisplayName("모든 Menu 를 조회한다")
    @Test
    void list() {
        // given
        MenuProduct menuProduct = MenuProductFactory.builder().build();
        Menu menu = MenuFactory.builder().build();
        given(menuDao.findAll()).willReturn(Collections.singletonList(menu));
        given(menuProductDao.findAllByMenuId(menu.getId())).willReturn(
            Collections.singletonList(menuProduct));

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).containsExactly(menu);
    }

    @Nested
    class CreateTest {

        private Product product;

        private MenuProduct menuProduct;

        private Menu menu;

        private Menu savedMenu;

        @BeforeEach
        void setUp() {
            product = ProductFactory.builder()
                .id(1L)
                .name("강정치킨")
                .price(new BigDecimal(17000))
                .build();

            menuProduct = MenuProductFactory.builder()
                .productId(1L)
                .quantity(2L)
                .build();

            menu = MenuFactory.builder()
                .name("후라이드+후라이드")
                .price(new BigDecimal(19000))
                .menuGroupId(1L)
                .menuProducts(Collections.singletonList(menuProduct))
                .build();

            savedMenu = MenuFactory.copy(menu)
                .id(1L)
                .build();
        }

        @DisplayName("Menu 를 생성한다")
        @Test
        void create() {
            // given
            given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
            given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
            given(menuDao.save(menu)).willReturn(savedMenu);
            given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

            // when
            Menu result = menuService.create(menu);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedMenu);
        }

        @DisplayName("Menu 생성 실패한다 - price 가 null 인 경우")
        @Test
        void createFail_whenPriceIsNull() {
            // given
            menu = MenuFactory.copy(menu)
                .price(null)
                .build();

            // when
            ThrowingCallable throwingCallable = () -> menuService.create(menu);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu 생성 실패한다 - price 가 음수인 경우")
        @Test
        void createFail_whenPriceIsNegative() {
            // given
            menu = MenuFactory.copy(menu)
                .price(new BigDecimal(-1))
                .build();

            // when
            ThrowingCallable throwingCallable = () -> menuService.create(menu);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu 생성 실패한다 - menuGroupId 에 해당하는 menuGroup 이 존재하지 않는 경우")
        @Test
        void createFail_whenMenuGroupIsNotExisting() {
            // given
            given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

            // when
            ThrowingCallable throwingCallable = () -> menuService.create(menu);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu 생성 실패한다 - menuProduct 의 productId 에 해당하는 product 가 존재하지 않는 경우")
        @Test
        void createFail_whenProductDoesNotExist() {
            // given
            given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
            given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.empty());

            // when
            ThrowingCallable throwingCallable = () -> menuService.create(menu);

            //then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu 생성 실패한다 - menu 의 price 가 모든 product 의 총 금액보다 큰 경우")
        @Test
        void createFail_whenMenuPriceIsBiggerThanProductPriceSum() {
            // given
            product = ProductFactory.copy(product)
                .price(new BigDecimal(1000))
                .build();
            menuProduct = MenuProductFactory.copy(menuProduct)
                .quantity(1)
                .build();
            menu = MenuFactory.copy(menu)
                .price(new BigDecimal(1001))
                .menuProducts(Collections.singletonList(menuProduct))
                .build();
            given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
            given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

            // when
            ThrowingCallable throwingCallable = () -> menuService.create(menu);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
