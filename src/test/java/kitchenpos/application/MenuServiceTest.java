package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.fixture.Fixture.MenuGroupId;
import kitchenpos.fixture.Fixture.ProductId;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를")
    @Nested
    class CreateTest {

        private static final String MENU_NAME = "앙념 두마리 치킨";

        private final List<MenuProductRequest> menuProductRequests = List.of(
            new MenuProductRequest(ProductId.후라이드, 2),
            new MenuProductRequest(ProductId.양념치킨, 1)
        );

        @DisplayName("등록한다.")
        @Test
        void create() {
            // given
            BigDecimal price = BigDecimal.valueOf(20000);
            MenuRequest menuRequest = new MenuRequest(
                MENU_NAME,
                price,
                MenuGroupId.두마리_메뉴,
                menuProductRequests
            );

            // when
            MenuResponse actual = menuService.create(menuRequest);

            // then
            Long menuId = actual.getId();
            List<MenuProductResponse> menuProducts = actual.getMenuProducts();
            assertAll(
                () -> assertThat(menuId).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(MENU_NAME),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(price),
                () -> assertThat(menuProducts)
                    .extracting("menuId")
                    .containsOnly(menuId),
                () -> assertThat(menuProducts)
                    .extracting("productId")
                    .containsOnly(ProductId.후라이드, ProductId.양념치킨),
                () -> assertThat(menuProducts)
                    .extracting("quantity")
                    .containsOnly(ProductId.후라이드, ProductId.양념치킨)
            );
        }

        @DisplayName("0원 미만으로 등록할 수 없다.")
        @Test
        void overZero() {
            // given
            BigDecimal price = BigDecimal.valueOf(-1);
            MenuRequest menuRequest = new MenuRequest(
                MENU_NAME,
                price,
                MenuGroupId.두마리_메뉴,
                menuProductRequests
            );

            // when then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0원 미만으로 메뉴를 등록할 수 없습니다.");
        }

        @DisplayName("없는 메뉴 그룹으로 등록할 수 없다.")
        @Test
        void notExistGroup() {
            // given
            BigDecimal price = BigDecimal.valueOf(20000);
            MenuRequest menuRequest = new MenuRequest(
                MENU_NAME,
                price,
                MenuGroupId.없는_메뉴_그룹,
                menuProductRequests
            );

            // when then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
        }

        @DisplayName("상품 가격들 합보다 큰 가격으로 등록할 수 없다.")
        @Test
        void invalidPrice() {
            // given
            BigDecimal price = BigDecimal.valueOf(48100);
            MenuRequest menuRequest = new MenuRequest(
                MENU_NAME,
                price,
                MenuGroupId.두마리_메뉴,
                menuProductRequests
            );

            // when then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 상품들 가격 합보다 클 수 없습니다.");
        }
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertAll(
            () -> assertThat(actual).hasSize(6),
            () -> assertThat(actual)
                .extracting("name")
                .containsOnly("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨")
        );
    }
}
