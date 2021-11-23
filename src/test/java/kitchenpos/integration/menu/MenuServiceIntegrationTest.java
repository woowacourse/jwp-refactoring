package kitchenpos.integration.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.application.dto.request.MenuGroupRequestDto;
import kitchenpos.application.dto.request.MenuProductRequestDto;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.request.ProductRequestDto;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        ProductResponseDto productResponseDto = productService
            .create(new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000)));
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService
            .create(new MenuGroupRequestDto("시즌 메뉴"));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            productResponseDto.getId(),
            2L
        );
        MenuRequestDto requestDto = new MenuRequestDto(
            "얌 프라이",
            BigDecimal.valueOf(8000),
            menuGroupResponseDto.getId(),
            Collections.singletonList(menuProduct)
        );

        // when
        MenuResponseDto responseDto = menuService.create(requestDto);

        // then
        assertThat(responseDto.getId()).isNotNull();
        assertThat(responseDto.getMenuProducts()).hasSize(1);
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다. - null인 경우")
    @Test
    void create_InvalidPriceWithNull_Fail() {
        // given
        ProductResponseDto productResponseDto = productService
            .create(new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000)));
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService
            .create(new MenuGroupRequestDto("시즌 메뉴"));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            productResponseDto.getId(),
            2L
        );
        MenuRequestDto requestDto = new MenuRequestDto(
            "얌 프라이",
            null,
            menuGroupResponseDto.getId(),
            Collections.singletonList(menuProduct)
        );

        // when
        // then
        assertThatThrownBy(() -> menuService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다. - 0 이하인 경우")
    @Test
    void create_InvalidPriceWithNegative_Fail() {
        // given
        ProductResponseDto productResponseDto = productService
            .create(new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000)));
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService
            .create(new MenuGroupRequestDto("시즌 메뉴"));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            productResponseDto.getId(),
            2L
        );
        MenuRequestDto requestDto = new MenuRequestDto(
            "얌 프라이",
            BigDecimal.valueOf(-1000),
            menuGroupResponseDto.getId(),
            Collections.singletonList(menuProduct)
        );

        // when
        // then
        assertThatThrownBy(() -> menuService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴 상품이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingMenuProduct_Fail() {
        // given
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService
            .create(new MenuGroupRequestDto("시즌 메뉴"));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            1L,
            2L
        );
        MenuRequestDto requestDto = new MenuRequestDto(
            "얌 프라이",
            BigDecimal.valueOf(-1000),
            menuGroupResponseDto.getId(),
            Collections.singletonList(menuProduct)
        );

        // when
        // then
        assertThatThrownBy(() -> menuService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴 그룹이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingMenuGroup_Fail() {
        // given
        ProductResponseDto productResponseDto = productService
            .create(new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000)));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            productResponseDto.getId(),
            2L
        );
        MenuRequestDto requestDto = new MenuRequestDto(
            "얌 프라이",
            null,
            1L,
            Collections.singletonList(menuProduct)
        );

        // when
        // then
        assertThatThrownBy(() -> menuService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void list_Valid_Success() {
        // given
        ProductResponseDto productResponseDto = productService
            .create(new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000)));
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService
            .create(new MenuGroupRequestDto("시즌 메뉴"));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            productResponseDto.getId(),
            2L
        );
        MenuRequestDto requestDto = new MenuRequestDto(
            "얌 프라이",
            BigDecimal.valueOf(8000),
            menuGroupResponseDto.getId(),
            Collections.singletonList(menuProduct)
        );

        menuService.create(requestDto);

        // when
        java.util.List<MenuResponseDto> responses = menuService.list();

        // then
        assertThat(responses).hasSize(1);
    }
}
