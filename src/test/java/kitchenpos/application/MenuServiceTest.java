package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.menu.service.MenuService;
import kitchenpos.domain.menu.service.dto.MenuCreateRequest;
import kitchenpos.domain.menu.service.dto.MenuGroupResponse;
import kitchenpos.domain.menu.service.dto.MenuResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.fixture.MenuFixture.menu;
import static kitchenpos.application.fixture.MenuGroupFixture.western;
import static kitchenpos.application.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.application.fixture.ProductFixture.noodle;
import static kitchenpos.application.fixture.ProductFixture.potato;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Nested
    class Create {

        @Test
        void 메뉴를_생성한다() {
            // given
            final Product noodle = noodle();
            final Product potato = potato();
            final MenuProduct wooDong = menuProduct(noodle, 1);
            final MenuProduct frenchFries = menuProduct(potato, 1);
            final MenuGroup menuGroup = western();
            final Menu expected = spy(menu("우동세트", BigDecimal.valueOf(9000), menuGroup, List.of(wooDong, frenchFries)));

            given(menuGroupRepository.findById(anyLong())).willReturn(Optional.ofNullable(menuGroup));
            given(menuProductRepository.fetchAllById(anyList())).willReturn(List.of(wooDong, frenchFries));
            given(menuRepository.save(any(Menu.class))).willReturn(expected);
            final long savedId = 1L;
            given(expected.getId()).willReturn(savedId);

            // when
            final MenuCreateRequest request = new MenuCreateRequest("우동", 1L, 1L, List.of(1L, 2L));
            final MenuResponse actual = menuService.create(request);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                    () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice().getPrice().longValue()),
                    () -> assertMenuGroup(actual.getMenuGroup(), expected.getMenuGroup()),
                    () -> assertThat(actual.getMenuProductResponses()).hasSize(expected.getMenuProducts().getMenuProducts().size())
            );
        }

        @Test
        void 메뉴_가격이_0보다_작으면_생성할_수_없다() {
            // given
            final long underZeroPrice = -1L;
            final MenuCreateRequest request = new MenuCreateRequest("우동세트", underZeroPrice, 1L, List.of(1L, 2L));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품이_저장되어_있지_않으면_메뉴를_생성할_수_없다() {
            // given
            final MenuCreateRequest request = new MenuCreateRequest("우동세트", 9000L, 1L, List.of(1L, 2L));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 메뉴를_전체_조회할_수_있다() {
            // when
            menuService.list();

            // then
            verify(menuRepository, only()).findAll();
        }
    }

    private void assertMenuGroup(final MenuGroupResponse actual, final MenuGroup expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

}
