package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("메뉴 상품 레파지토리 테스트")
class MenuProductRepositoryTest {

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 메뉴_아이디를_통해_메뉴_상품_목록을_조회한다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
        final Product product = productRepository.save(ProductFixture.상품_엔티티_생성());
        final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, List.of(product)));

        // when
        final List<MenuProduct> actual = menuProductRepository.findAllByMenuId(menu.getId());

        // then
        assertThat(actual).isEqualTo(menu.getMenuProducts());
    }
}
