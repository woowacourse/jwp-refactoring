package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private List<MenuProduct> createDummyMenuProducts() {
        return List.of(new MenuProduct(1L, 2));
    }

    @Nested
    @DisplayName("메뉴 생성 로직을 테스트한다.")
    class create{

        @Test
        @DisplayName("메뉴를 생성한다.")
        void create() {
            final Menu menu = new Menu("test menu name",
                    BigDecimal.valueOf(32000),
                    1L,
                    createDummyMenuProducts());

            final Menu actual = menuService.create(menu);

            assertAll(
                    () -> assertThat(actual.getName()).isEqualTo(menu.getName()),
                    () -> assertThat(actual.getPrice().intValue()).isEqualTo(BigDecimal.valueOf(32000).intValue()),
                    () -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                    () -> assertThat(actual.getMenuProducts()).hasSize(1)
            );
        }

        @Test
        @DisplayName("가격이 음수라면 예외를 발생시킨다.")
        void create_negativePrice() {
            final Menu menu = new Menu(
                    "test menu name",
                    BigDecimal.valueOf(-10000),
                    1L,
                    createDummyMenuProducts());
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 menuGroup 이라면 예외를 발생시킨다.")
        void create_noMenuGroup() {
            final Menu menu = new Menu(
                    "test menu name",
                    BigDecimal.valueOf(10000),
                    9999999L,
                    createDummyMenuProducts());
            assertThatThrownBy(() -> menuService.create(menu))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품을 따로 판매할때보다 묶음으로 팔때 가격이 비싼 경우 예외를 발생시킨다.")
        void create_lowerThanIndividuallyProductSellingPrice(){
            final MenuProduct menuProduct = new MenuProduct(1L, 1L);
            menuProduct.setQuantity(2);
            final Menu menu = new Menu(
                    "test menu name",
                    BigDecimal.valueOf(999999999),
                    1L,
                    List.of(menuProduct));

            assertThatThrownBy(() -> menuService.create(menu))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 내의 개별 상품이 존재하지 않을떄 예외를 발생시킨다.")
        void create_noMenuProduct(){
            final Menu menu = new Menu(
                    "test menu name",
                    BigDecimal.valueOf(10000),
                    1L,
                    null);

            assertThatThrownBy(() -> menuService.create(menu))
                            .isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        final List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(6);
    }
}
