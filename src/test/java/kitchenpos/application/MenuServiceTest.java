package kitchenpos.application;

import kitchenpos.Menu.application.MenuService;
import kitchenpos.Menu.domain.Menu;
import kitchenpos.Menu.domain.MenuProduct;
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

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @ParameterizedTest
    @DisplayName("가격이 올바르지 않으면 Menu를 등록할 수 없다.")
    @MethodSource("minusAndNullPrice")
    public void priceException(BigDecimal price) {
        //given & when
        Menu menu = new Menu("invalidMenu", price,
                menuGroup.getId(), Collections.singletonList(new MenuProduct(product1.getId(), 1L)));

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
        Menu menu = new Menu("invalidMenu", BigDecimal.valueOf(1000),
                Long.MAX_VALUE, Collections.singletonList(new MenuProduct(product1.getId(), 1L)));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu에 속하는 MenuProduct의 Product가 존재하지 않으면 등록할 수 없다.")
    public void emptyMenuProductException() {
        //given & when
        Menu menu = new Menu("invalidMenu", BigDecimal.valueOf(1000),
                menuGroup.getId(), Collections.singletonList(new MenuProduct(Long.MAX_VALUE, 1L)));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu의 가격은 Menu에 들어가는 모든 Product 가격들의 합 보다 높아서는 안된다.")
    public void menuPriceLowerThanProductSumException() {
        //given & when
        Menu menu = new Menu("invalidMenu", BigDecimal.valueOf(1001),
                menuGroup.getId(), Collections.singletonList(new MenuProduct(product1.getId(), 1L)));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu를 등록할 수 있다.")
    public void enrollMenu() {
        //given & when
        Menu menu = new Menu("validMenu", BigDecimal.valueOf(1000),
                menuGroup.getId(), Collections.singletonList(new MenuProduct(product1.getId(), 1L)));

        //then
        assertDoesNotThrow(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("존재하는 Menu를 조회할 수 있다.")
    public void findAll() {
        //given & when
        List<Menu> list = menuService.list();

        //then
        assertThat(list).hasSize(1);
    }
}