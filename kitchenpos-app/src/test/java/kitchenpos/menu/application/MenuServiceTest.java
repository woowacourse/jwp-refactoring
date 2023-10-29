package kitchenpos.menu.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.dto.MenuCreateRequest;
import kitchenpos.application.menu.dto.MenuResponse;
import kitchenpos.common.ServiceTest;
import kitchenpos.common.fixtures.MenuFixtures;
import kitchenpos.common.fixtures.MenuGroupFixtures;
import kitchenpos.common.fixtures.ProductFixtures;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menugroup.exception.MenuGroupException;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.product.exception.ProductException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("메뉴 생성 시")
    class CreateMenu {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            menuGroupRepository.save(MenuGroupFixtures.MENU_GROUP1());
            productRepository.save(ProductFixtures.PRODUCT1());
            productRepository.save(ProductFixtures.PRODUCT2());

            final MenuCreateRequest request = MenuFixtures.MENU1_REQUEST();

            // when
            final MenuResponse response = menuService.create(request);

            // then
            assertThat(response.getId()).isNotNull();
        }


        @Test
        @DisplayName("메뉴 그룹 ID에 해당하는 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundMenuGroupById() {
            productRepository.save(ProductFixtures.PRODUCT1());
            productRepository.save(ProductFixtures.PRODUCT2());

            final MenuCreateRequest request = MenuFixtures.MENU1_REQUEST();

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(MenuGroupException.NotFoundMenuGroupException.class)
                    .hasMessage("[ERROR] 해당하는 MENU GROUP을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("메뉴 상품 목록의 각 메뉴 상품의 상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundProductById() {
            // given
            menuGroupRepository.save(MenuGroupFixtures.MENU_GROUP1());

            final MenuCreateRequest request = MenuFixtures.MENU1_REQUEST();

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(ProductException.NotFoundProductException.class)
                    .hasMessage("[ERROR] 해당하는 PRODUCT를 찾을 수 없습니다.");
        }
    }
}
