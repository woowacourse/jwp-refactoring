package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class JdbcTemplateMenuDaoTest {

    @Autowired
    private JdbcTemplateMenuDao menuDao;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = MenuFixture.아메리카노(1L, null);
    }

    @Test
    void 메뉴를_등록한다() {
        // when
        Menu savedMenu = menuDao.save(menu);

        // then
        assertThat(savedMenu).usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(menu);
    }

    @Test
    void 메뉴id_로_메뉴를_조회한다() {
        // when
        Menu savedMenu = menuDao.save(menu);
        Menu foundMenu = menuDao.findById(savedMenu.getId()).get();

        // then
        assertThat(foundMenu).usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(menu);
    }

    @Test
    void 전체_메뉴_목록을_조회한다() {
        // given
        int originSize = menuDao.findAll().size();

        // when
        menuDao.save(menu);
        List<Menu> savedMenus = menuDao.findAll();

        // then
        assertThat(savedMenus).hasSize(originSize + 1);
    }

    @Test
    void 메뉴id_목록_중_존재하는_id의_개수를_조회한다() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L);

        // when
        long count = menuDao.countByIdIn(ids);

        // then
        assertThat(count).isEqualTo(3);
    }
}
