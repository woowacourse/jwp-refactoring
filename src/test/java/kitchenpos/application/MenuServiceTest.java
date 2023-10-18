package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static kitchenpos.fixture.MenuFixture.한마리메뉴_DTO;
import static kitchenpos.fixture.MenuFixture.후라이드치킨;
import static kitchenpos.fixture.ProductFixture.후라이드_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceIntegrationTest {

    @Nested
    @DisplayName("Menu를 생성한다.")
    class create {

        @Test
        @DisplayName("정상적으로 생성한다.")
        void success() {
            final ProductDto savedProduct = productService.create(후라이드_DTO());
            final MenuProduct menuProduct = createMenuProduct(savedProduct, 1L);
            final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
            final Menu menu = 후라이드치킨(savedMenuGroupDto, List.of(menuProduct));

            final Menu savedMenu = menuService.create(menu);

            assertAll(
                () -> assertThat(savedMenu)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "menuProducts.seq", "price")
                    .isEqualTo(menu),
                () -> assertThat(savedMenu.getPrice())
                    .isEqualByComparingTo(menu.getPrice())
            );
        }

        @Test
        @DisplayName("가격이 0미만인 경우 예외처리.")
        void throwExceptionPriceLowerThan0() {
            final ProductDto savedProductDto = productService.create(후라이드_DTO());
            final MenuProduct menuProduct = createMenuProduct(savedProductDto, 1L);
            final MenuGroupDto savedMenuGroup = menuGroupService.create(한마리메뉴_DTO());
            final Menu menu = 후라이드치킨(savedMenuGroup, List.of(menuProduct));

            menu.setPrice(BigDecimal.valueOf(-1000));

            //when
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("MenuGroup이 저장되지 않은 경우 예외처리")
        void throwExceptionMenuGroupIsNotExist() {
            final ProductDto savedProductDto = productService.create(후라이드_DTO());
            final MenuProduct menuProduct = createMenuProduct(savedProductDto, 1L);
            final MenuGroupDto unSavedMenuGroupDto = 한마리메뉴_DTO();
            final Menu menu = 후라이드치킨(unSavedMenuGroupDto, List.of(menuProduct));

            //when
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("product가 저장되지 않은 경우 예외처리")
        void throwExceptionProductIsNotExist() {
            final ProductDto unSavedProductDto = 후라이드_DTO();
            final MenuProduct menuProduct = createMenuProduct(unSavedProductDto, 1L);
            final MenuGroupDto savedMenuGroup = menuGroupService.create(한마리메뉴_DTO());
            final Menu menu = 후라이드치킨(savedMenuGroup, List.of(menuProduct));

            //when
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("price가 product의 총합보다 큰 경우 예외처리")
        void throwExceptionPriceIsBiggerThanProductSum() {
            final ProductDto savedProductDto = productService.create(후라이드_DTO());
            final MenuProduct menuProduct = createMenuProduct(savedProductDto, 1L);
            final MenuGroupDto savedMenuGroup = menuGroupService.create(한마리메뉴_DTO());

            final Menu menu = 후라이드치킨(savedMenuGroup, List.of(menuProduct));
            menu.setPrice(BigDecimal.valueOf(18000));

            //when
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("menu list를 조회한다.")
    void list() {
        final ProductDto savedProductDto = productService.create(후라이드_DTO());
        final MenuProduct menuProduct = createMenuProduct(savedProductDto, 1L);
        final MenuGroupDto savedMenuGroup = menuGroupService.create(한마리메뉴_DTO());
        final Menu menu = 후라이드치킨(savedMenuGroup, List.of(menuProduct));

        final Menu savedMenu = menuService.create(menu);

        //when
        final List<Menu> menus = menuService.list();

        assertAll(
            () -> assertThat(menus)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("price")
                .containsExactly(savedMenu),
            () -> assertThat(menus.get(0).getPrice())
                .isEqualByComparingTo(savedMenu.getPrice())
        );
    }
}
