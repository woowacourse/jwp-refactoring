package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 등록 기능")
    @Nested
    class CreateTest {

        @DisplayName("정상 작동")
        @Test
        void create() {
            final MenuRequest menuRequest = new MenuRequest("페퍼로니", BigDecimal.valueOf(1000L), menuGroup.getId(),
                    List.of(new MenuProductRequest(product1.getId(), 1L)));

            final MenuResponse menuResponse = menuService.create(menuRequest);
            final Optional<Menu> savedMenu = menuDao.findById(menuResponse.getId());
            assertThat(savedMenu).isPresent();
        }

        @DisplayName("메뉴의 가격이 null 이거나 음수이면 예외가 발생한다.")
        @ParameterizedTest(name = "메뉴의 가격이 {0} 이면 예외가 발생한다.")
        @NullSource
        @ValueSource(strings = {"-1"})
        void create_Exception_Price(BigDecimal price) {
            final MenuRequest menuRequest = new MenuRequest("페퍼로니", price, menuGroup.getId(),
                    List.of(new MenuProductRequest(product1.getId(), 1L)));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격이 null 이거나 음수이면 안됩니다.");
        }

        @DisplayName("메뉴가 속한 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
        @Test
        void create_Exception_NonExistMemberGroup() {
            final MenuRequest menuRequest = new MenuRequest("페퍼로니", BigDecimal.valueOf(1000L), Long.MAX_VALUE,
                    List.of(new MenuProductRequest(product1.getId(), 1L)));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴가 속한 메뉴그룹이 존재하지 않습니다.");
        }

        @DisplayName("메뉴에 등록하려는 상품이 존재하지 않는 상품이면 예외가 발생한다.")
        @Test
        void create_Exception_NonExistProduct() {
            final MenuRequest menuRequest = new MenuRequest("페퍼로니", BigDecimal.valueOf(1000L), menuGroup.getId(),
                    List.of(new MenuProductRequest(Long.MAX_VALUE, 1L)));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴에 등록하려는 상품이 존재하지 않습니다.");
        }

        @DisplayName("메뉴의 가격이 메뉴의 상품들 * 가격보다 크면 예외가 발생한다.")
        @Test
        void create_Exception_SumOfPrice() {
            final MenuRequest menuRequest = new MenuRequest("페퍼로니", product1.getPrice().add(BigDecimal.ONE),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product1.getId(), 1L)));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격이 메뉴의 상품들 * 가격보다 크면 안됩니다.");
        }
    }

    @DisplayName("모든 메뉴와 메뉴에 속한 메뉴상품들을 불러온다.")
    @Test
    void list() {
        final List<MenuResponse> menuResponses = menuService.list();

        final Optional<MenuResponse> menuResponse = menuResponses.stream()
                .filter(it -> it.getId().equals(menu.getId()))
                .findAny();

        assertAll(
                () -> assertThat(menuResponse).isPresent(),
                () -> assertThat(menuResponse.get().getMenuProducts())
                        .usingElementComparatorIgnoringFields("seq")
                        .containsExactly(MenuProductResponse.from(menuProducts.get(0)),
                                MenuProductResponse.from(menuProducts.get(1))));
    }
}
