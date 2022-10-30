package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;

class MenuRepositoryTest extends RepositoryTest {

    @Test
    void 메뉴를_저장한다() {
        Menu menu = new Menu("", BigDecimal.valueOf(0L), 1L);

        Menu savedMenu = menuRepository.save(menu, List.of());

        assertThat(menuDao.findById(savedMenu.getId())).isPresent();
    }

    @Test
    void 메뉴_목록을_가져온다() {
        Menu menu = new Menu("", BigDecimal.valueOf(0), 1L);
        int beforeSize = menuRepository.findAll().size();
        menuDao.save(menu);

        assertThat(menuRepository.findAll().size()).isEqualTo(beforeSize + 1);
    }
}
