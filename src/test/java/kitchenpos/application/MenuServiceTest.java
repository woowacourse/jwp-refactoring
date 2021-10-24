package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.request.MenuProductRequestDto;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuService 단위 테스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("Menu가 속한 MenuGroup이 존재하지 않는다면")
        @Nested
        class Context_menu_group_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                MenuRequestDto menuRequestDto =
                    new MenuRequestDto("kevin", BigDecimal.valueOf(-1), 1L, Collections.emptyList());
                given(menuGroupRepository.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Menu가 속한 MenuGroup이 존재하지 않습니다.");

                verify(menuGroupRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("Menu 가격이 null이면")
        @Nested
        class Context_price_null {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                MenuRequestDto menuRequestDto =
                    new MenuRequestDto("kevin", null, 1L, Collections.emptyList());
                MenuGroup menuGroup = new MenuGroup(1L, "kevin group");
                given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroup));

                // when, then
                assertThatCode(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("유효하지 않은 Menu 가격입니다.");

                verify(menuGroupRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("Menu 가격이 음수면")
        @Nested
        class Context_price_negative {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                MenuRequestDto menuRequestDto =
                    new MenuRequestDto("kevin", BigDecimal.valueOf(-1), 1L, Collections.emptyList());
                MenuGroup menuGroup = new MenuGroup(1L, "kevin group");
                given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroup));

                // when, then
                assertThatCode(() -> menuService.create(menuRequestDto))
                    .hasMessage("유효하지 않은 Menu 가격입니다.");

                verify(menuGroupRepository, times(1)).findById(1L);

            }
        }

        @DisplayName("Menu에 속한 MenuProduct와 연결된 Product를 조회할 수 없으면")
        @Nested
        class Context_product_of_menu_product_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                List<MenuProductRequestDto> menuProductRequestDtos = Arrays.asList(
                    new MenuProductRequestDto(1L, 10L),
                    new MenuProductRequestDto(2L, 10L)
                );
                MenuRequestDto menuRequestDto =
                    new MenuRequestDto("kevin", BigDecimal.valueOf(300), 1L, menuProductRequestDtos);
                MenuGroup menuGroup = new MenuGroup(1L, "kevin group");
                given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroup));

                // when, then
                assertThatCode(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product가 존재하지 않습니다.");

                verify(menuGroupRepository, times(1)).findById(1L);
                verify(productRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("Menu 가격이 MenuProduct들의 누계를 초과하면")
        @Nested
        class Context_menu_product_sum_gt_menu_price {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                List<MenuProductRequestDto> menuProductRequestDtos = Arrays.asList(
                    new MenuProductRequestDto(1L, 3L),
                    new MenuProductRequestDto(2L, 2L)
                );
                MenuRequestDto menuRequestDto =
                    new MenuRequestDto("kevin", BigDecimal.valueOf(61), 1L, menuProductRequestDtos);
                MenuGroup menuGroup = new MenuGroup(1L, "kevin group");
                Product product1 = new Product(1L, "김밥", BigDecimal.valueOf(10));
                Product product2 = new Product(2L, "누룽지", BigDecimal.valueOf(15));
                given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroup));
                given(productRepository.findById(1L)).willReturn(Optional.of(product1));
                given(productRepository.findById(2L)).willReturn(Optional.of(product2));

                // when, then
                assertThatCode(() -> menuService.create(menuRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Menu 가격은 Product 가격 누계를 초과할 수 없습니다.");

                verify(menuGroupRepository, times(1)).findById(1L);
                verify(productRepository, times(2)).findById(anyLong());
            }
        }

        //
        @DisplayName("Menu 가격이 0 이상 양수 및 MemberProduct 누계 이하이고 MenuGroup이 존재하면")
        @Nested
        class Context_other_valid_case {

            @DisplayName("Menu를 정상 등록한다.")
            @ParameterizedTest
            @ValueSource(ints = {59, 60})
            void it_saves_and_returns_menu(int price) {
                // given
                List<MenuProductRequestDto> menuProductRequestDtos = Arrays.asList(
                    new MenuProductRequestDto(1L, 3L),
                    new MenuProductRequestDto(2L, 2L)
                );
                MenuRequestDto menuRequestDto =
                    new MenuRequestDto("kevin", BigDecimal.valueOf(price), 1L, menuProductRequestDtos);
                MenuGroup menuGroup = new MenuGroup(1L, "kevin group");
                Product product1 = new Product(1L, "김밥", BigDecimal.valueOf(10));
                Product product2 = new Product(2L, "누룽지", BigDecimal.valueOf(15));
                Menu expected =
                    new Menu(1L, "kevin", BigDecimal.valueOf(price), menuGroup, new ArrayList<>());
                List<MenuProduct> menuProducts = Arrays.asList(
                    new MenuProduct(1L, expected, product1, 3),
                    new MenuProduct(2L, expected, product2, 2)
                );
                expected.updateMenuProducts(menuProducts);
                given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroup));
                given(productRepository.findById(1L)).willReturn(Optional.of(product1));
                given(productRepository.findById(2L)).willReturn(Optional.of(product2));
                given(menuRepository.save(any(Menu.class)))
                    .willReturn(expected);

                // when
                MenuResponseDto response = menuService.create(menuRequestDto);

                // then
                assertThat(response.getId()).isOne();
                assertThat(response.getMenuProductResponseDtos()).hasSize(2)
                    .extracting("id", "quantity")
                    .containsExactly(tuple(1L, 3L), tuple(2L, 2L));

                verify(menuGroupRepository, times(1)).findById(1L);
                verify(productRepository, times(2)).findById(anyLong());
                verify(menuRepository, times(1)).save(any(Menu.class));
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("Menu 목록을 조회한다.")
        @Test
        void it_returns_menu_list() {
            // given
            MenuGroup menuGroup = new MenuGroup(1L, "kevin group");
            Product product1 = new Product(1L, "김밥", BigDecimal.valueOf(10));
            Product product2 = new Product(2L, "누룽지", BigDecimal.valueOf(15));
            Menu expected =
                new Menu(1L, "kevin", BigDecimal.valueOf(10L), menuGroup, new ArrayList<>());
            List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, expected, product1, 3),
                new MenuProduct(2L, expected, product2, 2)
            );
            expected.updateMenuProducts(menuProducts);
            given(menuRepository.findAll()).willReturn(Arrays.asList(expected));

            // when
            List<MenuResponseDto> response = menuService.list();

            // then
            assertThat(response).hasSize(1);

            verify(menuRepository, times(1)).findAll();
        }
    }
}
