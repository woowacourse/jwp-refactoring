package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuDao menuDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    private final static Long VALID_MENU_GROUP_ID = 1L;
    private final static Long INVALID_MENU_GROUP_ID = 1000L;

    private MenuProduct validMenuProduct;
    private MenuProduct invalidMenuProduct;

    @BeforeEach
    void setUp() {
        validMenuProduct = new MenuProduct();
        validMenuProduct.setProductId(1L);
        validMenuProduct.setQuantity(2);

        invalidMenuProduct = new MenuProduct();
        invalidMenuProduct.setProductId(9999L);
        invalidMenuProduct.setQuantity(2);
    }

    @ParameterizedTest
    @DisplayName("가격이 올바르지 않으면 Menu를 등록할 수 없다.")
    @MethodSource("minusAndNullPrice")
    public void priceException(BigDecimal price) {
        //given
        Menu menu = new Menu();
        menu.setName("올바르지않은가격의메뉴");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(validMenuProduct));

        //when
        menu.setPrice(price);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> minusAndNullPrice() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of((Object) null)
        );
    }

    @Test
    @DisplayName("존재하는 MenuGroup에 속해있지 않으면 Menu를 등록할 수 없다.")
    public void menuGroupException() {
        //given
        Menu menu = new Menu();
        menu.setName("menuGroup이상있는메뉴");
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuProducts(Collections.singletonList(validMenuProduct));

        //when
        menu.setMenuGroupId(INVALID_MENU_GROUP_ID);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu에 속하는 MenuProduct의 Product가 존재하지 않으면 등록할 수 없다.")
    public void emptyMenuProductException() {
        //given
        Menu menu = new Menu();
        menu.setName("존재하지않는product로조리하는메뉴");
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(VALID_MENU_GROUP_ID);

        //when
        menu.setMenuProducts(Collections.singletonList(invalidMenuProduct));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu의 가격은 Menu에 들어가는 모든 Product 가격들의 합 보다 높아서는 안된다.")
    public void menuPriceLowerThanProductSumException() {
        //given
        Menu menu = new Menu();
        menu.setName("총product비용보다비싼메뉴");
        menu.setMenuProducts(Collections.singletonList(validMenuProduct));
        menu.setMenuGroupId(VALID_MENU_GROUP_ID);

        //when
        menu.setPrice(BigDecimal.valueOf(32001));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu를 등록할 수 있다.")
    public void enrollMenu() {
        //given
        Menu menu = new Menu();
        menu.setName("Menu");
        menu.setMenuProducts(Collections.singletonList(validMenuProduct));
        menu.setMenuGroupId(VALID_MENU_GROUP_ID);
        menu.setPrice(BigDecimal.valueOf(10000));

        //then
        assertDoesNotThrow(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("Menu 생성 후에 Menu의 MenuProduct 내에 menu_id를 할당해주어야 한다.")
    public void enrollMenuAndAllocateMenuIdIntoMenuProduct() {
        //given
        Menu menu = new Menu();
        menu.setName("Menu");
        menu.setMenuProducts(Collections.singletonList(validMenuProduct));
        menu.setMenuGroupId(VALID_MENU_GROUP_ID);
        menu.setPrice(BigDecimal.valueOf(10000));

        //when
        Menu savedMenu = menuService.create(menu);

        //then
        for (MenuProduct menuProduct : savedMenu.getMenuProducts()) {
            assertThat(menuProduct.getMenuId()).isNotNull();
        }
    }

    @Test
    @DisplayName("존재하는 Menu를 조회할 수 있다.")
    public void findAll() {
        //given & when
        List<Menu> list = menuService.list();

        //then
        assertThat(list).hasSize(6);
    }
}