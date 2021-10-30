package kitchenpos.service;

import static kitchenpos.service.fixture.MenuGroupFixture.한마리메뉴;
import static kitchenpos.service.fixture.ProductFixture.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.service.fixture.MenuFixture;
import kitchenpos.service.fixture.MenuGroupFixture;
import kitchenpos.service.fixture.MenuProductFixture;
import kitchenpos.service.fixture.ProductFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 기능에서 ")
class MenuServiceTest {

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuDao = MenuFixture.createFixture().getTestMenuDao();
        menuGroupDao = MenuGroupFixture.createFixture().getTestMenuGroupDao();
        menuProductDao = MenuProductFixture.createFixture().getTestMenuProductDao();
        productDao = ProductFixture.createFixture().getTestProductDao();

        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("메뉴를 생성할 때 ")
    @Nested
    class CreateTest {

        public static final String DOUBLE_FRIED_CHICKEN_NAME = "후라이드+후라이드";

        private final BigDecimal productPrice = new BigDecimal(16000);
        private final long notExistMenuGroupId = 0L;

        @DisplayName("성공한다")
        @Test
        void successTest() {
            //given
            Menu menu = new Menu();
            menu.setName(DOUBLE_FRIED_CHICKEN_NAME);
            menu.setPrice(productPrice);
            menu.setMenuGroupId(한마리메뉴);

            MenuProduct fried = menuProductDao.findById(MenuProductFixture.후라이드).get();
            menu.setMenuProducts(Collections.singletonList(fried));

            //when
            Menu expectedResult = menuService.create(menu);

            //than
            Assertions.assertAll(
                () -> assertThat(expectedResult.getName()).isEqualTo(DOUBLE_FRIED_CHICKEN_NAME),
                () -> assertThat(expectedResult.getPrice()).isEqualTo(productPrice)
            );
        }

        @DisplayName("액수가 0 보다 작으면 예외가 발생한다")
        @Test
        void whenPriceSmallerThanZero() {
            //given
            Menu menu = new Menu();
            menu.setName(DOUBLE_FRIED_CHICKEN_NAME);
            menu.setPrice(new BigDecimal(-1000));
            menu.setMenuGroupId(후라이드);

            MenuProduct fried = menuProductDao.findById(MenuProductFixture.후라이드).get();
            menu.setMenuProducts(Collections.singletonList(fried));

            //when & than
            assertThatThrownBy(()-> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹 ID가 없으면 예외가 발생한다")
        @Test
        void whenMenuGroupIdIsNull() {
            //given
            Menu menu = new Menu();
            menu.setName(DOUBLE_FRIED_CHICKEN_NAME);
            menu.setPrice(productPrice);
            menu.setMenuGroupId(notExistMenuGroupId);

            MenuProduct fried = menuProductDao.findById(MenuProductFixture.후라이드).get();
            menu.setMenuProducts(Collections.singletonList(fried));

            //when & than
            assertThatThrownBy(()-> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("속한 상품들의 가격 합 보다 메뉴의 가격이 더 작으면 예외")
        @Test
        void whenMenuGroupPriceSmallerThanMenuPriceSum() {
            //given
            Menu menu = new Menu();
            menu.setName(DOUBLE_FRIED_CHICKEN_NAME);
            menu.setPrice(new BigDecimal(productPrice.intValue() + 1));
            menu.setMenuGroupId(후라이드);

            MenuProduct fried = menuProductDao.findById(MenuProductFixture.후라이드).get();
            menu.setMenuProducts(Collections.singletonList(fried));

            //when & than
            assertThatThrownBy(()-> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴명으로 메뉴 목록을 받아올 수 있다")
    @Test
    void listTest() {
        //when
        List<Menu> list = menuService.list();
        //then
        List<Menu> expectedFixtures = MenuFixture.createFixture().getFixtures();

        assertThat(list.size()).isEqualTo(expectedFixtures.size());
        // menuProducts를 제외하고 Fixture의 요소 중 내용이 다 같은 픽스쳐가 있는지 검증
        expectedFixtures.forEach(
            menu -> assertThat(list).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("menuProducts")
                .contains(menu)
        );
    }
}