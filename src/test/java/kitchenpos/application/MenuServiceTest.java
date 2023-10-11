package kitchenpos.application;

import static kitchenpos.common.MenuFixtures.MENU1_MENU_GROUP_ID;
import static kitchenpos.common.MenuFixtures.MENU1_MENU_PRODUCT;
import static kitchenpos.common.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.MenuFixtures.MENU1_PRICE;
import static kitchenpos.common.MenuFixtures.MENU1_REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.exception.MenuGroupException;
import kitchenpos.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Nested
    @DisplayName("메뉴 생성 시")
    class CreateMenu {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            final Menu menu = MENU1_REQUEST();

            // when
            final Menu createdMenu = menuService.create(menu);

            // then
            assertSoftly(softly -> {
                softly.assertThat(createdMenu.getId()).isNotNull();
                softly.assertThat(createdMenu.getName()).isEqualTo(menu.getName());
                softly.assertThat(createdMenu.getPrice()).isEqualTo(menu.getPrice());
                softly.assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
                softly.assertThat(createdMenu.getMenuProducts()).usingRecursiveComparison()
                        .ignoringFields("seq").isEqualTo(menu.getMenuProducts());
            });
        }

        @Test
        @DisplayName("메뉴 가격이 존재하지 않으면 예외가 발생한다.")
        void throws_WhenMenuPriceNull() {
            // given
            final Menu menu = new Menu();
            menu.setName(MENU1_NAME);
            menu.setMenuGroupId(MENU1_MENU_GROUP_ID);
            menu.setMenuProducts(List.of(MENU1_MENU_PRODUCT()));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴 가격은 null 또는 0 미만의 값일 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE})
        @DisplayName("메뉴 가격이 0 미만이면 예외가 발생한다.")
        void throws_WhenMenuPriceNull(final int negativePrice) {
            // given
            final Menu menu = new Menu();
            menu.setName(MENU1_NAME);
            menu.setMenuGroupId(MENU1_MENU_GROUP_ID);
            menu.setMenuProducts(List.of(MENU1_MENU_PRODUCT()));
            menu.setPrice(BigDecimal.valueOf(negativePrice));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴 가격은 null 또는 0 미만의 값일 수 없습니다.");
        }

        @Test
        @DisplayName("메뉴 그룹 ID에 해당하는 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundMenuGroupById() {
            // given
            final Long notExistMenuGroupId = -1L;
            final Menu menu = MENU1_REQUEST();
            menu.setMenuGroupId(notExistMenuGroupId);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(MenuGroupException.NotFoundMenuGroupException.class)
                    .hasMessage("[ERROR] 해당하는 MENU GROUP을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("메뉴 상품 목록의 각 메뉴 상품의 상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundProductById() {
            // given
            final Long notExistProductId = -1L;
            final Menu menu = MENU1_REQUEST();
            final MenuProduct menuProduct = menu.getMenuProducts().get(0);
            menuProduct.setProductId(notExistProductId);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(ProductException.NotFoundProductException.class)
                    .hasMessage("[ERROR] 해당하는 PRODUCT를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("메뉴 가격이 메뉴 상품 가격 합보다 크면 예외가 발생한다.")
        void throws_menuPriceLargeThanSum() {
            // given
            final Menu menu = MENU1_REQUEST();
            final MenuProduct menuProduct = menu.getMenuProducts().get(0);
            long quantity = menuProduct.getQuantity();
            long menuProductPrice = MENU1_PRICE.longValue();
            menu.setPrice(BigDecimal.valueOf(quantity * menuProductPrice + 1));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴의 가격이 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
    }
}
