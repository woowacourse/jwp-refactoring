package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.exception.KitchenPosException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @Nested
    class create {

        @Test
        void 메뉴_그룹_식별자에_대한_메뉴_그룹이_없으면_예외() {
            // given
            var request = new MenuCreateRequest(1000, 4885L, "맥주세트", List.of(
                new MenuProductCreateRequest(10, 1L)
            ));

            // when
            given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("해당 메뉴 그룹이 없습니다. menuGroupId=4885");
        }

        @Test
        void 상품_식별자에_대한_상품이_없으면_예외() {
            // given
            var request = new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(1, 1L),
                new MenuProductCreateRequest(1, 2L),
                new MenuProductCreateRequest(1, 4885L),
                new MenuProductCreateRequest(1, 4886L)
            ));
            MenuGroup menuGroup = new MenuGroup(1L, "주류");
            given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(menuGroup));

            // when
            given(productRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    new Product(1L, "맥주", Money.from(500)),
                    new Product(2L, "소주", Money.from(500))
                ));

            // then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("존재하지 않는 상품이 있습니다. notExistProductIds=[4885, 4886]");
        }

        @Test
        void 메뉴_상품의_가격_총합보다_메뉴의_가격이_크면_예외() {
            // given
            var request = new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(1, 1L)
            ));
            MenuGroup menuGroup = new MenuGroup(1L, "주류");
            given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(menuGroup));
            given(productRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(new Product(1L, "맥주", Money.from(999))));
            given(menuRepository.save(any(Menu.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

            // when
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품의 총합 가격보다 작아야 합니다.");
        }

        @Test
        void 성공() {
            // given
            var request = new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
                new MenuProductCreateRequest(1, 1L)
            ));
            MenuGroup menuGroup = new MenuGroup(1L, "주류");
            given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(menuGroup));
            given(productRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(new Product(1L, "맥주", Money.from(1000))));
            given(menuRepository.save(any(Menu.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

            // when
            var actual = menuService.create(request);

            // then
            assertThat(actual.getName()).isEqualTo("맥주세트");
        }
    }
}
