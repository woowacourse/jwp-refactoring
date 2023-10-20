package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 메뉴_등록_성공_저장() {
        // given
        final Product chicken = productRepository.save(ProductFixture.치킨_8000원());
        final Product pizza = productRepository.save(ProductFixture.피자_8000원());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));

        // when
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(8000), menuGroup, List.of(chicken, pizza))
        );

        // then
        assertThat(menuService.list())
                .map(Menu::getId)
                .filteredOn(id -> Objects.equals(id, menu.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("메뉴는 메뉴 그룹에 속한다.")
    void 메뉴_등록_실패_메뉴_그룹_없음() {
        // given
        final Product chicken = productRepository.save(ProductFixture.치킨_8000원());
        final Product pizza = productRepository.save(ProductFixture.피자_8000원());
        final MenuGroup menuGroup = new MenuGroup("존재하지 않는 메뉴 그룹");

        // when
        final Menu menu = 세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(10000), menuGroup, List.of(chicken, pizza));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
