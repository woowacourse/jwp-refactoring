package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.exception.InvalidMenuGroupException;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.ui.dto.request.menu.MenuProductRequestDto;
import kitchenpos.ui.dto.request.menu.MenuRequestDto;
import kitchenpos.ui.dto.response.menu.MenuResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("[성공] 새로운 메뉴 틍록")
    @Test
    void create_success() {
        // given
        MenuRequestDto menu = newMenu();

        // when
        MenuResponseDto createdMenu = menuService.create(menu);

        // then
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu)
            .extracting("name", "menuGroupId")
            .contains(menu.getName(), menu.getMenuGroupId());
        assertThat(createdMenu.getMenuProducts())
            .extracting("menuId", "productId", "quantity")
            .contains(tuple(
                createdMenu.getId(), newMenuProduct().getProductId(), newMenuProduct().getQuantity()
            ));
    }

    @DisplayName("[실패] 가격이 null 이면 예외 발생")
    @Test
    void create_nullPrice_ExceptionThrown() {
        // given
        MenuRequestDto menu = newMenu(null);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(InvalidPriceException.class)
            .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @DisplayName("[실패] 가격이 0보다 작으면 예외 발생")
    @Test
    void create_negativePrice_ExceptionThrown() {
        // given
        MenuRequestDto menu = newMenu(BigDecimal.valueOf(-5));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(InvalidPriceException.class)
            .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @DisplayName("[실패] 메뉴 가격이 개별 상품 가격의 합보다 비싸면 예외 발생")
    @Test
    void create_tooExpensivePrice_ExceptionThrown() {
        // given
        MenuRequestDto menu = newMenu(BigDecimal.valueOf(50_000));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(InvalidPriceException.class)
            .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @DisplayName("[실패] 존재하지 않는 메뉴 그룹 ID면 예외 발생")
    @Test
    void create_invalidMenuGroupId_ExceptionThrown() {
        // given
        MenuRequestDto menu = newMenuWithGroupId(100L);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(InvalidMenuGroupException.class)
            .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
    }

    @DisplayName("[성공] 전체 메뉴 조회")
    @Test
    void list_success() {
        // given
        int previousSize = menuService.list().size();
        menuService.create(newMenu());

        // when
        List<MenuResponseDto> result = menuService.list();

        // then
        assertThat(result).hasSize(previousSize + 1);
    }

    private MenuRequestDto newMenu() {
        return newMenu(BigDecimal.valueOf(25_000));
    }

    private MenuRequestDto newMenu(BigDecimal price) {
        return new MenuRequestDto(
            "새로운 메뉴",
            price,
            1L,
            Collections.singletonList(newMenuProduct())
        );
    }

    private MenuRequestDto newMenuWithGroupId(Long menuGroupId) {
        return new MenuRequestDto(
            "새로운 메뉴",
            BigDecimal.valueOf(25_000),
            menuGroupId,
            Collections.singletonList(newMenuProduct())
        );
    }

    private MenuProductRequestDto newMenuProduct() {
        return new MenuProductRequestDto(1L, 2L);
    }
}
