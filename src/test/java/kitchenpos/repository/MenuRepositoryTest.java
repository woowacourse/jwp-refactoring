package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.repository.support.RepositoryTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuRepositoryTest extends RepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 메뉴_개수_검색() {
        // given
        Menu menu = defaultMenu();
        long nonexistenceMenuId = 3L;

        // when
        long actual = menuRepository.countByIdIn(List.of(menu.getMenuGroupId(), nonexistenceMenuId));

        // then
        Assertions.assertThat(actual).isEqualTo(1L);
    }

}
