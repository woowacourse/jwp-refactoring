package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.request.MenuCreateRequest;
import kitchenpos.dto.menu.request.MenuProductRequest;
import kitchenpos.dto.menu.response.MenuGroupResponse;
import kitchenpos.dto.menu.response.MenuResponse;
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

            private final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.builder()
                    .name("햄버거 세트")
                    .build());
            private final Product product = productRepository.save(Product.builder()
                    .name("햄버거")
                    .price(BigDecimal.valueOf(5000))
                    .build());
            private final MenuCreateRequest request = new MenuCreateRequest(
                    "햄버거 메뉴",
                    BigDecimal.valueOf(5000),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1)));

            @Test
            void 메뉴를_추가한다() {
                MenuResponse actual = menuService.create(request);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getName()).isEqualTo("햄버거 메뉴");
                });
            }
        }

        @Nested
        class 메뉴의_가격이_null_일_경우 {

            private final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.builder()
                    .name("햄버거 세트")
                    .build());
            private final Product product = productRepository.save(Product.builder()
                    .name("햄버거")
                    .price(BigDecimal.valueOf(5000))
                    .build());
            private final MenuCreateRequest request = new MenuCreateRequest(
                    "햄버거 메뉴",
                    null,
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 이거나 0원 미만일 수 없습니다.");
            }
        }

        @Nested
        class 메뉴의_가격이_0원_미만일_경우 {

            private final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.builder()
                    .name("햄버거 세트")
                    .build());
            private final Product product = productRepository.save(Product.builder()
                    .name("햄버거")
                    .price(BigDecimal.valueOf(5000))
                    .build());
            private final MenuCreateRequest request = new MenuCreateRequest(
                    "햄버거 메뉴",
                    BigDecimal.valueOf(-1),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 이거나 0원 미만일 수 없습니다.");
            }
        }

        @Nested
        class 존재하지_않는_메뉴_그룹에_추가할_경우 {

            private final Product product = productRepository.save(Product.builder()
                    .name("햄버거")
                    .price(BigDecimal.valueOf(5000))
                    .build());
            private final MenuCreateRequest request = new MenuCreateRequest(
                    "햄버거 메뉴",
                    null,
                    0L,
                    List.of(new MenuProductRequest(product.getId(), 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴 그룹이 존재하지 않습니다.");
            }
        }

        @Nested
        class 메뉴의_가격이_메뉴에_속한_상품의_합보다_클_경우 {

            private final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.builder()
                    .name("햄버거 세트")
                    .build());
            private final Product product = productRepository.save(Product.builder()
                    .name("햄버거")
                    .price(BigDecimal.valueOf(4999))
                    .build());
            private final MenuCreateRequest request = new MenuCreateRequest(
                    "햄버거 메뉴",
                    BigDecimal.valueOf(5000),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
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
                List<MenuGroupResponse> responses = menuGroupService.list();

                assertThat(responses).isNotEmpty();
            }
        }
    }
}
