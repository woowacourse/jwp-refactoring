package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = BigDecimal.valueOf(5000);
            private final MenuGroupResponse menuGroupResponse = menuGroupService.create(
                    new MenuGroupCreateRequest("햄버거 세트"));
            private final ProductResponse productResponse = productService.create(
                    new ProductCreateRequest(name, price));
            private final MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 1);
            private final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(name, price,
                    menuGroupResponse.getId(), List.of(menuProductRequest));

            @Test
            void 메뉴를_추가한다() {
                MenuResponse actual = menuService.create(menuCreateRequest);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getName()).isEqualTo(name);
                });
            }
        }

        @Nested
        class 메뉴의_가격이_null_일_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = null;
            private final MenuGroupResponse menuGroupResponse = menuGroupService.create(
                    new MenuGroupCreateRequest("햄버거 세트"));
            private final ProductResponse productResponse = productService.create(
                    new ProductCreateRequest(name, BigDecimal.valueOf(5000)));
            private final MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 1);
            private final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(name, price,
                    menuGroupResponse.getId(), List.of(menuProductRequest));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 이거나 0원 미만일 수 없습니다.");
            }
        }

        @Nested
        class 메뉴의_가격이_0원_미만일_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = BigDecimal.valueOf(-1);
            private final MenuGroupResponse menuGroupResponse = menuGroupService.create(
                    new MenuGroupCreateRequest("햄버거 세트"));
            private final ProductResponse productResponse = productService.create(
                    new ProductCreateRequest(name, BigDecimal.valueOf(5000)));
            private final MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 1);
            private final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(name, price,
                    menuGroupResponse.getId(), List.of(menuProductRequest));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 이거나 0원 미만일 수 없습니다.");
            }
        }

        @Nested
        class 존재하지_않는_메뉴_그룹에_추가할_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = BigDecimal.valueOf(5000);
            private final MenuGroupResponse menuGroupResponse = menuGroupService.create(
                    new MenuGroupCreateRequest("햄버거 세트"));
            private final ProductResponse productResponse = productService.create(
                    new ProductCreateRequest(name, price));
            private final MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 1);
            private final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(name, price,
                    0L, List.of(menuProductRequest));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴 그룹이 존재하지 않습니다.");
            }
        }

        @Nested
        class 메뉴의_가격이_메뉴에_속한_상품의_합보다_클_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = BigDecimal.valueOf(5000);
            private final MenuGroupResponse menuGroupResponse = menuGroupService.create(
                    new MenuGroupCreateRequest("햄버거 세트"));
            private final ProductResponse productResponse = productService.create(
                    new ProductCreateRequest(name, BigDecimal.valueOf(4999)));
            private final MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 1);
            private final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(name, price,
                    menuGroupResponse.getId(), List.of(menuProductRequest));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴의 가격은 메뉴에 속한 상품의 합보다 클 수 없습니다.");
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            @Test
            void 메뉴_목록을_반환한다() {
                List<MenuGroup> menuGroups = menuGroupService.list();

                assertThat(menuGroups).isNotEmpty();
            }
        }
    }
}
