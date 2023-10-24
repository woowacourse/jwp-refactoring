package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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
@DisplayName("메뉴 레파지토리 테스트")
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void 메뉴_아이디_리스트에서_존재하는_아이디_개수를_반환한다() {
        // given
        final Long unsavedId = 999L;
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
        final Product product = productRepository.save(ProductFixture.상품_엔티티_생성());
        final List<Menu> menus = menuRepository.saveAll(MenuFixture.메뉴_엔티티들_생성(4, menuGroup, List.of(product)));

        final List<Long> ids = List.of(unsavedId, menus.get(0).getId(), menus.get(1).getId(), menus.get(2).getId());

        // when
        final long actual = menuRepository.countByIdIn(ids);

        // then
        assertThat(actual).isEqualTo(3);
    }
}
