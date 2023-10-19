package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenuProductDto;
import static kitchenpos.fixture.MenuFixture.한마리메뉴_DTO;
import static kitchenpos.fixture.MenuFixture.후라이드치킨_DTO;
import static kitchenpos.fixture.ProductFixture.후라이드_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.ProductDto;
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
            final MenuProductDto menuProductDto = createMenuProductDto(savedProduct, 1L);
            final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
            final MenuDto menuDto = 후라이드치킨_DTO(
                savedMenuGroupDto, List.of(menuProductDto), BigDecimal.valueOf(16000)
            );

            final MenuDto savedMenuDto = menuService.create(menuDto);

            assertAll(
                () -> assertThat(savedMenuDto)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "menuProducts.seq", "menuProducts.menuId", "price")
                    .isEqualTo(menuDto),
                () -> assertThat(savedMenuDto.getPrice())
                    .isEqualByComparingTo(menuDto.getPrice())
            );
        }

        @Test
        @DisplayName("가격이 0미만인 경우 예외처리.")
        void throwExceptionPriceLowerThan0() {
            final ProductDto savedProductDto = productService.create(후라이드_DTO());
            final MenuProductDto menuProductDto = createMenuProductDto(savedProductDto, 1L);
            final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
            final MenuDto menuDto = 후라이드치킨_DTO(
                savedMenuGroupDto, List.of(menuProductDto), BigDecimal.valueOf(-16000)
            );

            //when
            assertThatThrownBy(() -> menuService.create(menuDto))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("product가 저장되지 않은 경우 예외처리")
        void throwExceptionProductIsNotExist() {
            final ProductDto unSavedProductDto = 후라이드_DTO();
            final MenuProductDto menuProductDto = createMenuProductDto(unSavedProductDto, 1L);
            final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
            final MenuDto menuDto = 후라이드치킨_DTO(
                savedMenuGroupDto, List.of(menuProductDto), BigDecimal.valueOf(-16000)
            );

            //when
            assertThatThrownBy(() -> menuService.create(menuDto))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("price가 product의 총합보다 큰 경우 예외처리")
        void throwExceptionPriceIsBiggerThanProductSum() {
            final ProductDto savedProduct = productService.create(후라이드_DTO());
            final MenuProductDto menuProductDto = createMenuProductDto(savedProduct, 1L);
            final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
            final MenuDto menuDto = 후라이드치킨_DTO(
                savedMenuGroupDto, List.of(menuProductDto), BigDecimal.valueOf(18000)
            );

            //when
            assertThatThrownBy(() -> menuService.create(menuDto))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("menu list를 조회한다.")
    void list() {
        final ProductDto savedProduct = productService.create(후라이드_DTO());
        final MenuProductDto menuProductDto = createMenuProductDto(savedProduct, 1L);
        final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
        final MenuDto menuDto = 후라이드치킨_DTO(
            savedMenuGroupDto, List.of(menuProductDto), BigDecimal.valueOf(16000)
        );

        final MenuDto savedMenuDto = menuService.create(menuDto);

        //when
        final List<MenuDto> menuDtos = menuService.list();

        assertAll(
            () -> assertThat(menuDtos)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("price")
                .containsExactly(savedMenuDto),
            () -> assertThat(menuDtos.get(0).getPrice())
                .isEqualByComparingTo(savedMenuDto.getPrice())
        );
    }
}
