package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Test
    void 메뉴를_성공적으로_저장한다() {
        // given
        Menu menu = 추천메뉴_후추칰힌_2개();

        // when
        Long savedMenuId = menuService.create(menu)
                .getId();

        // then
        Menu savedMenu = menuDao.findById(savedMenuId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(savedMenu).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedMenu);
    }

}
