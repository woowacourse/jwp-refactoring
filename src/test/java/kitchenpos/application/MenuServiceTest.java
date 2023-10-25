package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private static final Product product1 = new Product(1L, "제품1", BigDecimal.TEN);
    private static final Product product2 = new Product(2L, "제품2", BigDecimal.TEN);

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        // given
        final long menuGroupId = 1L;
        given(menuGroupRepository.existsById(menuGroupId))
            .willReturn(true);

        final MenuProduct menuProduct1 = new MenuProduct(1L, product1, 1L);
        final MenuProduct menuProduct2 = new MenuProduct(2L, product2, 2L);
        given(productRepository.getById(any()))
            .willReturn(product1, product2);

        final Menu menu = new Menu(1L, "메뉴", List.of(menuProduct1, menuProduct2));
        given(menuRepository.save(any(Menu.class)))
            .willReturn(menu);

        // when
        final MenuCreateRequest request = new MenuCreateRequest("메뉴", 30, menuGroupId, List.of(
            new MenuProductCreateRequest(1L, 1),
            new MenuProductCreateRequest(2L, 2)
        ));
        final MenuResponse created = menuService.create(request);

        // then
        assertThat(created.getId()).isEqualTo(menu.getId());
        assertThat(created.getName()).isEqualTo(request.getName());
        assertThat(created.getPrice())
            .isEqualTo(
                new MenuProducts(List.of(menuProduct1, menuProduct2), menu).calculatePrice().getValue().longValue());
    }

    @DisplayName("메뉴의 MemberGroupId 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotExistMemberGroupId() {
        // given
        final Long notExistedMemberGroupId = 0L;

        // when
        // then
        assertThatThrownBy(
            () -> menuService.create(new MenuCreateRequest("메뉴", 10000, notExistedMemberGroupId, List.of(
                new MenuProductCreateRequest(1L, 1),
                new MenuProductCreateRequest(2L, 2)
            )))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 Product 가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void create_failNotExistProduct() {
        // given
        given(menuGroupRepository.existsById(any(Long.class)))
            .willReturn(true);

        given(productRepository.getById(any()))
            .willThrow(IllegalArgumentException.class);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest("메뉴", 10000, 1L, List.of(
            new MenuProductCreateRequest(1L, 1),
            new MenuProductCreateRequest(2L, 2)
        )))).isInstanceOf(IllegalArgumentException.class);
    }
}
