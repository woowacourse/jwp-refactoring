package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

class MenuServiceTest extends BaseServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup menuGroup;
    private Product productD;
    private Product productS;
    private Product productT;
    private List<MenuProductRequest> menuProductRequests;
    private MenuRequest menuRequest;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("분식"));
        productD = productRepository.save(new Product("떡볶이", BigDecimal.TEN));
        productS = productRepository.save(new Product("순대", BigDecimal.ONE));
        productT = productRepository.save(new Product("튀김", BigDecimal.TEN));

        menuProductRequests = List.of(
                new MenuProductRequest(productD.getId(), 2L),
                new MenuProductRequest(productS.getId(), 1L),
                new MenuProductRequest(productT.getId(), 1L)
        );

        menuRequest = new MenuRequest("떡순튀", new BigDecimal(31), menuGroup.getId(), menuProductRequests);
    }

    @Nested
    @DisplayName("메뉴 생성 테스트")
    class MenuCreateTest {

        @Test
        @DisplayName("메뉴가 정상적으로 생성된다.")
        void createMenuWithValidData() {
            // Given & When
            Menu createdMenu = menuService.create(menuRequest);

            // then
            assertSoftly(softly -> {
                softly.assertThat(createdMenu.getPrice()).isEqualByComparingTo(menuRequest.getPrice());
                softly.assertThat(createdMenu.getMenuGroup().getId()).isEqualTo(menuRequest.getMenuGroupId());
                softly.assertThat(createdMenu.getMenuProducts().getMenuProducts().size())
                        .isEqualTo(menuRequest.getMenuProductRequests().size());
            });
        }

        @Test
        @DisplayName("가격이 null일 때 예외 발생한다.")
        void createMenuWithNullPrice() {
            // given
            final MenuRequest request = new MenuRequest("가격없는메뉴", null, menuGroup.getId(), menuProductRequests);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수일 때 예외 발생한다.")
        void createProductWithSubZeroPrice() {
            // Given
            BigDecimal negativePrice = BigDecimal.valueOf(-10);
            final MenuRequest request = new MenuRequest("음수가격메뉴", negativePrice, menuGroup.getId(),
                    menuProductRequests);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 그룹 ID가 존재하지 않을 때 예외 발생")
        void createProductWithUnExistedMenuGroupId() {
            // given
            final MenuRequest request = new MenuRequest("메뉴", BigDecimal.valueOf(31), 999L, menuProductRequests);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴의 가격이 메뉴 상품들의 합계보다 클 때 예외 발생")
        void createdMenuWithDifferentPrice() {
            final MenuRequest request = new MenuRequest("음수가격메뉴", BigDecimal.valueOf(999), menuGroup.getId(), menuProductRequests);

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("모든 메뉴를 반환한다.")
    void listTest() {
        assertThatCode(() -> menuService.list())
                .doesNotThrowAnyException();
    }
}
