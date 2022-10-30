package kitchenpos.application;

import static kitchenpos.support.MenuFixture.메뉴_생성;
import static kitchenpos.support.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.support.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuRequestDto;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

class MenuServiceTest extends ServiceTest {

    private ProductResponse product;
    private MenuGroup savedMenuGroup;

    @BeforeEach
    void setup() {
        product = 상품_등록(상품);
        savedMenuGroup = 메뉴_그룹_등록(메뉴_그룹);
    }

    @Nested
    @DisplayName("메뉴 생성 로직을 테스트한다.")
    class create {

        @Test
        @DisplayName("메뉴를 생성한다.")
        void create() {
            final MenuRequestDto menuRequestDto = 메뉴_생성(
                    "메뉴 이름",
                    BigDecimal.valueOf(9000),
                    savedMenuGroup.getId(),
                    product
            );

            final MenuResponse actual = menuService.create(menuRequestDto);

            assertAll(
                    () -> assertThat(actual.getName()).isEqualTo(menuRequestDto.getName()),
                    () -> assertThat(actual.getPrice().intValue()).isEqualTo(BigDecimal.valueOf(9000).intValue()),
                    () -> assertThat(actual.getMenuGroupId()).isEqualTo(menuRequestDto.getMenuGroupId()),
                    () -> assertThat(actual.getMenuProducts()).hasSize(1)
            );
        }

        @Test
        @DisplayName("가격이 음수라면 예외를 발생시킨다.")
        void create_negativePrice() {
            final MenuRequestDto menuRequestDto = 메뉴_생성(
                    "메뉴 이름",
                    BigDecimal.valueOf(-10000),
                    savedMenuGroup.getId(),
                    product
            );

            assertThatThrownBy(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 null이면 예외를 발생시킨다.")
        void create_nullPrice() {
            final MenuRequestDto menuRequestDto = 메뉴_생성(
                    "메뉴 이름",
                    null,
                    savedMenuGroup.getId(),
                    product
            );

            assertThatThrownBy(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("존재하지 않는 menuGroup 이라면 예외를 발생시킨다.")
        void create_noMenuGroup() {
            final MenuRequestDto menuRequestDto = 메뉴_생성(
                    "test menu name",
                    BigDecimal.valueOf(9000),
                    0L,
                    product
            );

            assertThatThrownBy(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품을 따로 판매할때보다 묶음으로 팔때 가격이 비싼 경우 예외를 발생시킨다.")
        void create_lowerThanIndividuallyProductSellingPrice() {
            final MenuRequestDto menuRequestDto = 메뉴_생성(
                    "메뉴 이름",
                    BigDecimal.valueOf(20000),
                    savedMenuGroup.getId(),
                    product
            );

            assertThatThrownBy(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 내의 개별 상품이 존재하지 않을떄 예외를 발생시킨다.")
        void create_noMenuProduct() {
            final MenuRequestDto menuRequestDto = 메뉴_생성(
                    "메뉴 이름",
                    BigDecimal.valueOf(9000),
                    savedMenuGroup.getId()
            );

            assertThatThrownBy(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        메뉴_등록(메뉴_생성("메뉴 이름1", BigDecimal.valueOf(9000), savedMenuGroup.getId(), product));
        메뉴_등록(메뉴_생성("메뉴 이름2", BigDecimal.valueOf(9000), savedMenuGroup.getId(), product));
        메뉴_등록(메뉴_생성("메뉴 이름3", BigDecimal.valueOf(9000), savedMenuGroup.getId(), product));

        final List<MenuResponse> actual = menuService.list();

        assertThat(actual).hasSize(3);
    }
}
