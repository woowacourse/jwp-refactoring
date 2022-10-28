package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴_그룹;
import static kitchenpos.fixture.ProductFixtures.간장치킨_상품;
import static kitchenpos.fixture.ProductFixtures.반반치킨_상품;
import static kitchenpos.fixture.ProductFixtures.순살치킨_상품;
import static kitchenpos.fixture.ProductFixtures.양념치킨_상품;
import static kitchenpos.fixture.ProductFixtures.통구이_상품;
import static kitchenpos.fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class MenuServiceTest {

    @Autowired
    private MenuService sut;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("메뉴 등록")
    class CreateTest {

        @DisplayName("메뉴 그룹이 null인 경우 등록할 수 없다.")
        @Test
        void createMenuWithNullMenuGroup() {
            final Product product = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 5L);
            final MenuRequest menuRequest = new MenuRequest("신메뉴", BigDecimal.valueOf(15000), null,
                    List.of(menuProductRequest));

            assertThatThrownBy(() -> sut.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 해당 메뉴가 속한 경우 등록할 수 없다.")
        @Test
        void createMenuWithNotExistMenuGroup() {
            final Product product = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 5L);
            final MenuRequest menuRequest = new MenuRequest("신메뉴", BigDecimal.valueOf(15000), -1L,
                    List.of(menuProductRequest));

            assertThatThrownBy(() -> sut.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 상품이 해당 메뉴에 속한 경우 등록할 수 없다.")
        @Test
        void createMenuWithNotExistMenuProduct() {
            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(-1L, 5L);
            final MenuRequest menuRequest = new MenuRequest("신메뉴", BigDecimal.valueOf(15000), menuGroup.getId(),
                    List.of(menuProductRequest));

            assertThatThrownBy(() -> sut.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void getMenus() {
        assertThat(sut.list())
                .hasSize(6)
                .extracting(Menu::getId, Menu::getName, menu -> menu.getPrice().intValue(), Menu::getMenuGroupId)
                .containsExactly(
                        tuple(1L, 후라이드_상품.getName() + "치킨", 후라이드_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(2L, 양념치킨_상품.getName(), 양념치킨_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(3L, 반반치킨_상품.getName(), 반반치킨_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(4L, 통구이_상품.getName(), 통구이_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(5L, 간장치킨_상품.getName(), 간장치킨_상품.getPrice(), 한마리메뉴_그룹.getId()),
                        tuple(6L, 순살치킨_상품.getName(), 순살치킨_상품.getPrice(), 한마리메뉴_그룹.getId())
                );
    }
}
