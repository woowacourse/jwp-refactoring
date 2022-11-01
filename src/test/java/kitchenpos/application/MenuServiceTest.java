package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.application.dto.MenuUpdateValuesRequest;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest {

    @Nested
    class 메뉴_생성 extends IntegrationTest {

        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuCreateRequest menu = new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1)));

            // when
            final MenuResponse extract = menuService.create(menu);

            // then
            assertThat(extract).isNotNull();
        }

        @Test
        void 요청에서_메뉴_금액이_0원_미만인_경우_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuCreateRequest menu = new MenuCreateRequest("짜장면", BigDecimal.valueOf(-1), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_메뉴_금액이_NULL_일_경우_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuCreateRequest menu = new MenuCreateRequest("짜장면", null, menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_메뉴를_추가할때_메뉴의_상품_번호가_존재하지_않는_번호일_경우_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final Long notRegisterProductId = 100L;
            final MenuCreateRequest menu = new MenuCreateRequest("짜장면", null, menuGroup.getId(),
                List.of(new MenuProductCreateRequest(notRegisterProductId, 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_추가한_메뉴가_메뉴_그룹에_속해있지_않은경우_예외가_발생한다() {
            // given
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuCreateRequest menu = new MenuCreateRequest("짜장면", null, null,
                List.of(new MenuProductCreateRequest(product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_추가한_메뉴가격의_합이_상품가격의_합보다_높을경우_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 0));
            final MenuCreateRequest menu = new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1)));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_수정 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));

            // when
            menuService.updateValues(menu.getId(), new MenuUpdateValuesRequest("짬뽕", BigDecimal.valueOf(1500)));

            // then
            final List<MenuResponse> extracts = menuService.list();
            assertAll(
                () -> assertThat(extracts.get(0).getName()).isEqualTo("짬뽕"),
                () -> assertThat(extracts.get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(1500))
            );
        }
    }
    /**
     * 현재 프로덕션에 추가되야 하는 방어로직
     * - MenuProduct에 대한 product ID가 동일한지를 검증
     */
}