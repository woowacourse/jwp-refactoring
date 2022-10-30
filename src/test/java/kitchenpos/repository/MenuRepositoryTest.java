package kitchenpos.repository;

import static kitchenpos.fixture.DomainFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuRepositoryTest extends RepositoryTest {

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupDao.save(createMenuGroup());
    }

    @Test
    void 메뉴를_저장한다() {
        Menu menu = new Menu("", BigDecimal.valueOf(0L), menuGroup.getId());

        Menu savedMenu = menuRepository.save(menu, List.of());

        assertThat(menuDao.findById(savedMenu.getId())).isPresent();
    }

    @Test
    void 메뉴_목록을_가져온다() {
        Menu menu = new Menu("", BigDecimal.valueOf(0), menuGroup.getId());
        int beforeSize = menuRepository.findAll().size();
        menuDao.save(menu);

        assertThat(menuRepository.findAll().size()).isEqualTo(beforeSize + 1);
    }
}
