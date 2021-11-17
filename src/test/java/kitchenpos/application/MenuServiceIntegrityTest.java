package kitchenpos.application;

import kitchenpos.SpringBootTestSupport;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.MenuFixture.*;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.ProductFixture.createProduct2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MenuServiceIntegrityTest extends SpringBootTestSupport {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 생성은")
    @Nested
    class Create {

        private MenuGroup menuGroup1;
        private Product product1;
        private Product product2;

        private MenuRequest request;
        private MenuProductRequest menuProductRequest;

        @BeforeEach
        void beforeAll() {
            menuGroup1 = save(createMenuGroup1());
            product1 = save(createProduct1());
            product2 = save(createProduct2());
            menuProductRequest = new MenuProductRequest(PRODUCT_ID, QUANTITY);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 속한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfNotExistGroup() {
            request = new MenuRequest(MENU_NAME1, MENU_PRICE, 0L, Collections.singletonList(menuProductRequest));

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 상품을 포함한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfNotExistProduct() {
            menuProductRequest = new MenuProductRequest(0L, QUANTITY);
            request = new MenuRequest(MENU_NAME1, MENU_PRICE, MENU_GROUP_ID, Collections.singletonList(menuProductRequest));

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 메뉴 상품 가격의 총합보다 클 수 없다.")
        @Test
        void createExceptionIfExceedPrice() {
            request = new MenuRequest(MENU_NAME1, BigDecimal.valueOf(100000), MENU_GROUP_ID, Arrays.asList(new MenuProductRequest(product1.getId(), 1), new MenuProductRequest(product2.getId(), 2)));

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 생성할 수 있다.")
        @Test
        void create() {
            request = new MenuRequest(MENU_NAME1, MENU_PRICE, menuGroup1.getId(), Arrays.asList(new MenuProductRequest(product1.getId(), 1), new MenuProductRequest(product2.getId(), 2)));

            MenuResponse actual = menuService.create(request);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(MENU_NAME1),
                    () -> assertThat(actual.getPrice()).isEqualTo(MENU_PRICE),
                    () -> assertThat(actual.getMenuGroup().getName()).isEqualTo(menuGroup1.getName()),
                    () -> assertThat(actual.getMenuProducts()).hasSize(2)
            );
        }
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup1 = save(createMenuGroup1());
        MenuGroup menuGroup2 = save(createMenuGroup2());
        Product product1 = save(createProduct1());
        Product product2 = save(createProduct2());
        Menu menu1 = save(createMenu1(menuGroup1, Collections.singletonList(product1)));
        Menu menu2 = save(createMenu2(menuGroup2, Collections.singletonList(product2)));

        List<MenuResponse> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0).getId()).isEqualTo(menu1.getId()),
                () -> assertThat(actual.get(0).getMenuProducts()).hasSize(1),
                () -> assertThat(actual.get(1).getId()).isEqualTo(menu2.getId()),
                () -> assertThat(actual.get(0).getMenuProducts()).hasSize(1)
        );
    }
}
