package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixtures.두마리메뉴_그룹;
import static kitchenpos.fixture.MenuGroupFixtures.신메뉴_그룹;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.fixture.ProductFixtures.간장치킨_상품;
import static kitchenpos.fixture.ProductFixtures.반반치킨_상품;
import static kitchenpos.fixture.ProductFixtures.순살치킨_상품;
import static kitchenpos.fixture.ProductFixtures.양념치킨_상품;
import static kitchenpos.fixture.ProductFixtures.통구이_상품;
import static kitchenpos.fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class MenuServiceTest {

    @Autowired
    private MenuService sut;

    @Nested
    @DisplayName("메뉴 등록")
    class CreateTest {

        @DisplayName("정상적인 경우 메뉴를 등록할 수 있다.")
        @Test
        void createMenu() {
            final MenuProduct menuProduct = 메뉴_상품_생성(통구이_상품.getId(), 5L);
            final Menu menu = new Menu("두마리메뉴", BigDecimal.valueOf(500), 신메뉴_그룹.getId(), List.of(menuProduct));

            final Menu actual = sut.create(menu);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getPrice().longValue()).isEqualTo(menu.getPrice().longValue()),
                    () -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                    () -> assertThat(actual.getMenuProducts()).hasSize(1)
            );
        }

        @DisplayName("메뉴 가격이 없는 경우 등록할 수 없다.")
        @Test
        void createMenuWithNullPrice() {
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = new Menu("두마리메뉴", null, 두마리메뉴_그룹.getId(), List.of(menuProduct));

            assertThatThrownBy(() -> sut.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 0원보다 적은 경우 등록할 수 없다.")
        @Test
        void createMenuWithPriceLessThanZero() {
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = 메뉴_생성("두마리메뉴", -1, 두마리메뉴_그룹.getId(), List.of(menuProduct));

            assertThatThrownBy(() -> sut.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 해당 메뉴가 속한 경우 등록할 수 없다.")
        @Test
        void createMenuWithNotExistMenuGroup() {
            final Long 존재하지_않는_메뉴_그룹_ID = -1L;

            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = 메뉴_생성("두마리메뉴", 500, 존재하지_않는_메뉴_그룹_ID, List.of(menuProduct));

            assertThatThrownBy(() -> sut.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 상품이 해당 메뉴에 속한 경우 등록할 수 없다.")
        @Test
        void createMenuWithNotExistMenuProduct() {
            final Long 존재하지_않는_상품_ID = -1L;

            final MenuProduct menuProduct = 메뉴_상품_생성(존재하지_않는_상품_ID, 5L);
            final Menu menu = 메뉴_생성("두마리메뉴", 500, 두마리메뉴_그룹.getId(), List.of(menuProduct));

            assertThatThrownBy(() -> sut.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 해당 메뉴에 속한 메뉴 상품들의 가격의 총합보다 크면 등록할 수 없다.")
        @Test
        void createMenuWithIncorrectPrice() {
            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = 메뉴_생성("두마리메뉴", 80_001, 두마리메뉴_그룹.getId(), List.of(menuProduct));

            assertThatThrownBy(() -> sut.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void getMenus() {
        assertThat(sut.list())
                .hasSize(6)
                .extracting(Menu::getId, Menu::getName, menu -> menu.getPrice().intValue(), Menu::getMenuGroupId)
                .containsExactly(
                        tuple(1L, 후라이드_상품.getName() + "치킨", 후라이드_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(2L, 양념치킨_상품.getName(), 양념치킨_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(3L, 반반치킨_상품.getName(), 반반치킨_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(4L, 통구이_상품.getName(), 통구이_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(5L, 간장치킨_상품.getName(), 간장치킨_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(6L, 순살치킨_상품.getName(), 순살치킨_상품.getPrice(), 한마리메뉴_그룹.getId())
                );
    }
}
