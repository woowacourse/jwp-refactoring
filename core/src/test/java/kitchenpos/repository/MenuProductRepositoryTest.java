package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.repository.support.RepositoryTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuProductRepositoryTest extends RepositoryTest {

    @Autowired
    MenuProductRepository menuProductRepository;

    @Autowired
    MenuRepository menuRepository;

    @Test
    void 메뉴의_메뉴_상품_검색() {
        // given
        Menu menu = defaultMenu();

        // when
        List<MenuProduct> actual = menuProductRepository.findAllByMenuId(menu.getId());

        // then
        assertThat(actual).hasSize(2);
    }
}
