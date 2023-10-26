package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.menu.request.MenuCreateRequest;
import kitchenpos.dto.menu.request.MenuProductCreateRequest;
import kitchenpos.dto.menugroup.request.MenuGroupCreateRequest;
import kitchenpos.repository.MenuRepository;
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
    MenuService menuService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuRepository menuRepository;

    @Nested
    @DisplayName("메뉴를 등록할 때")
    class CreateMenu {
        @Test
        @DisplayName("정상 등록된다.")
        void createMenu() {
            // given
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("menuGroupName");
            final Long menuGroupId = menuGroupService.create(menuGroupCreateRequest);

            final String name = "name";
            final BigDecimal price = BigDecimal.ZERO;
            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    name,
                    price,
                    menuGroupId,
                    Collections.emptyList()
            );

            // when
            final Long menuId = menuService.create(menuCreateRequest);

            // then
            assertThat(menuId).isPositive();
        }

        @ParameterizedTest(name = "price = {0}")
        @ValueSource(ints = {-1, 999999})
        @DisplayName("메뉴의 가격이 올바르지 않을 시 예외 발생")
        void menuPriceLessThanZeroWonException(final int price) {
            // given
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("menuGroupName");
            final Long menuGroupId = menuGroupService.create(menuGroupCreateRequest);

            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "name",
                    BigDecimal.valueOf(price),
                    menuGroupId,
                    Collections.emptyList()
            );

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menuCreateRequest));
        }

        @Test
        @DisplayName("메뉴의 가격이 null일 시 예외 발생")
        void menuPriceNullException() {
            // given
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("menuGroupName");
            final Long menuGroupId = menuGroupService.create(menuGroupCreateRequest);

            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "name",
                    null,
                    menuGroupId,
                    Collections.emptyList()
            );

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menuCreateRequest));
        }

        @Test
        @DisplayName("메뉴 그룹이 존재하지 않을 시 예외 발생")
        void menuGroupNotExistException() {
            // given
            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "name",
                    BigDecimal.ZERO,
                    -1L,
                    Collections.emptyList()
            );

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menuCreateRequest));
        }

        @Test
        @DisplayName("메뉴 상품이 존재하지 않을 시 예외 발생")
        void menuProductsNotExistException() {
            // given
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("menuGroupName");
            menuGroupService.create(menuGroupCreateRequest);

            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "name",
                    BigDecimal.ZERO,
                    -1L,
                    List.of(new MenuProductCreateRequest(-1L, 2))
            );

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menuCreateRequest));
        }
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다")
    void getMenus() {
        // given
        final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("menuGroupName");
        final Long menuGroupId = menuGroupService.create(menuGroupCreateRequest);

        final String name = "name";
        final BigDecimal price = BigDecimal.ZERO;
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                name,
                price,
                menuGroupId,
                Collections.emptyList()
        );

        // when
        final Long menuId = menuService.create(menuCreateRequest);

        // then
        final Menu menu = menuRepository.findById(menuId).get();
        assertSoftly(softly -> {
            softly.assertThat(menu.id()).isEqualTo(menuId);
            softly.assertThat(menu.name()).isEqualTo(name);
            softly.assertThat(menu.price().price().intValue()).isEqualTo(price.intValue());
            softly.assertThat(menu.menuGroup().id()).isEqualTo(menuGroupId);
        });
    }
}
