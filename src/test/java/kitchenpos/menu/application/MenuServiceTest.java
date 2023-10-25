package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.menu.application.request.MenuCreateRequest;
import kitchenpos.dto.ProductQuantityDto;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 메뉴를_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            Product product = productRepository.save(ProductFixture.createProductEntity(null, "김밥", 5_000));
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup(null, "한식"));
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "세트메뉴",
                    9_000,
                    menuGroup.getId(),
                    List.of(new ProductQuantityDto(product.getId(), 2))
            );

            MenuResponse menuResponse = menuService.create(menuCreateRequest);

            assertAll(
                    () -> assertThat(menuResponse.getId()).isPositive(),
                    () -> assertThat(menuResponse.getName()).isEqualTo("세트메뉴"),
                    () -> assertThat(menuResponse.getPrice()).isEqualTo("9000")
            );
        }

        @Test
        void 상품_가격이_0보다_작으면_예외가_발생한다() {
            int wrongPrice = -1;
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "세트메뉴",
                    wrongPrice,
                    1L,
                    List.of(new ProductQuantityDto(1L, 2))
            );

            assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("금액은 음수가 될 수 없습니다.");
        }

        @Test
        void 메뉴_그룹이_존재하지_않는_그룹이면_예외가_발생한다() {
            long wrongMenuGroupId = -1L;
            Product product = productRepository.save(ProductFixture.createProductEntity(null, "김밥", 5_000));
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "세트메뉴",
                    9_000,
                    wrongMenuGroupId,
                    List.of(new ProductQuantityDto(product.getId(), 2))
            );

            assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("일치하는 메뉴 그룹이 없습니다.");
        }

        @Test
        void 메뉴의_상품이_존재하지_않는_상품이면_예외가_발생한다() {
            long wrongProductId = -1L;
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup(null, "한식"));
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "세트메뉴",
                    9_000,
                    menuGroup.getId(),
                    List.of(new ProductQuantityDto(wrongProductId, 2))
            );

            assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("일치하는 상품을 찾을 수 없습니다.");
        }

        @Test
        void 메뉴의_가격이_각_상품_가격의_합보다_크면_예외가_발생한다() {
            Product product = productRepository.save(ProductFixture.createProductEntity(null, "김밥", 5_000));
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup(null, "한식"));
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "세트메뉴",
                    10_001,
                    menuGroup.getId(),
                    List.of(new ProductQuantityDto(product.getId(), 2))
            );

            assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 금액이 상품 금액 합계보다 클 수 없습니다.");
        }
    }

    @Test
    void 모든_메뉴를_조회한다() {
        Product product = productRepository.save(ProductFixture.createProductEntity(null, "김밥", 5_000));
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup(null, "한식"));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "세트메뉴",
                9_000,
                menuGroup.getId(),
                List.of(new ProductQuantityDto(product.getId(), 2))
        );
        menuService.create(menuCreateRequest);

        List<MenuResponse> menuResponses = menuService.list();

        assertThat(menuResponses).hasSizeGreaterThan(1);
    }
}
