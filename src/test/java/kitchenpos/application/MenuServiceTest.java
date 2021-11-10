package kitchenpos.application;

import kitchenpos.annotation.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@IntegrationTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private static final Long VALID_MENU_GROUP_ID = 1L;
    private static final Long INVALID_MENU_GROUP_ID = 1000L;

    private MenuProduct validMenuProduct;
    private MenuProduct invalidMenuProduct;

    @BeforeEach
    void setUp() {
        validMenuProduct = new MenuProduct(new Product(1L), 2L);
        invalidMenuProduct = new MenuProduct(new Product(9999L), 2L);
    }

    @ParameterizedTest
    @DisplayName("가격이 올바르지 않으면 Menu를 등록할 수 없다.")
    @MethodSource("minusAndNullPrice")
    public void priceException(BigDecimal price) {
        //given & when
        Menu menu = new Menu("invalidMenu", price,
                new MenuGroup(VALID_MENU_GROUP_ID), Collections.singletonList(validMenuProduct));

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
        //given & when
        Menu menu = new Menu("invalidMenu", BigDecimal.valueOf(10000),
                new MenuGroup(INVALID_MENU_GROUP_ID), Collections.singletonList(validMenuProduct));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu에 속하는 MenuProduct의 Product가 존재하지 않으면 등록할 수 없다.")
    public void emptyMenuProductException() {
        //given & when
        Menu menu = new Menu("invalidMenu", BigDecimal.valueOf(10000),
                new MenuGroup(VALID_MENU_GROUP_ID), Collections.singletonList(invalidMenuProduct));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu의 가격은 Menu에 들어가는 모든 Product 가격들의 합 보다 높아서는 안된다.")
    public void menuPriceLowerThanProductSumException() {
        //given & when
        Menu menu = new Menu("invalidMenu", BigDecimal.valueOf(32001),
                new MenuGroup(VALID_MENU_GROUP_ID), Collections.singletonList(validMenuProduct));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu를 등록할 수 있다.")
    public void enrollMenu() {
        //given & when
        Menu menu = new Menu("validMenu", BigDecimal.valueOf(10000),
                new MenuGroup(VALID_MENU_GROUP_ID), Collections.singletonList(validMenuProduct));

        //then
        assertDoesNotThrow(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("Menu 생성 후에 Menu의 MenuProduct 내에 menu_id를 할당해주어야 한다.")
    public void enrollMenuAndAllocateMenuIdIntoMenuProduct() {
        //given & when
        Menu menu = new Menu("validMenu", BigDecimal.valueOf(10000),
                new MenuGroup(VALID_MENU_GROUP_ID), Collections.singletonList(validMenuProduct));

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