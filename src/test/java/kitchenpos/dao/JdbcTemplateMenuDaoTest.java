package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateMenuDaoTest {

    private final MenuDao menuDao;

    @Autowired
    public JdbcTemplateMenuDaoTest(final DataSource dataSource) {
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @Test
    void 저장한다() {
        // given
        BigDecimal price = BigDecimal.valueOf(13000);
        Menu menu = new Menu(null, "pasta", price, 1L, new ArrayList<>());

        // when
        Menu savedMenu = menuDao.save(menu);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo("pasta"),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L)
        );
    }

    @Test
    void ID로_조회한다() {
        // given
        Long id = 1L;

        // when
        Optional<Menu> menu = menuDao.findById(id);

        // then
        Assertions.assertAll(
                () -> assertThat(menu).isPresent(),
                () -> {
                    Menu actual = menu.get();
                    Assertions.assertAll(
                            () -> assertThat(actual.getName()).isEqualTo("후라이드치킨"),
                            () -> assertThat(actual.getMenuGroupId()).isEqualTo(2L)
                    );
                }
        );
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        List<Menu> menus = menuDao.findAll();

        // when
        Assertions.assertAll(
                () -> assertThat(menus)
                        .hasSize(6)
                        .usingRecursiveComparison()
                        .ignoringFields("price", "menuProducts")
                        .isEqualTo(
                                Arrays.asList(
                                        new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(2L, "양념치킨", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(3L, "반반치킨", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(4L, "통구이", BigDecimal.valueOf(16000), 2L, new ArrayList<>()),
                                        new Menu(5L, "간장치킨", BigDecimal.valueOf(17000), 2L, new ArrayList<>()),
                                        new Menu(6L, "순살치킨", BigDecimal.valueOf(17000), 2L, new ArrayList<>())
                                )
                        )
        );
    }

    @Test
    void ID로_개수를_센다() {
        // given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // when
        long count = menuDao.countByIdIn(ids);

        // then
        assertThat(count).isEqualTo(3);
    }
}
