package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class MenuDaoTest {

    private MenuDao menuDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private RowMapper<Menu> rowMapper = (rs, rowNum) -> {
        Menu menu = new Menu();
        menu.setId(rs.getLong("id"));
        menu.setName(rs.getString("name"));
        menu.setPrice(rs.getBigDecimal("price"));
        menu.setMenuGroupId(rs.getLong("menu_group_id"));
        return menu;
    };

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(jdbcTemplate.getJdbcTemplate().getDataSource());
    }

    @Test
    void 메뉴를_저장한다() {
        // given
        Menu menu = new Menu();
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);

        // when
        Menu result = menuDao.save(menu);

        // then
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of("id", result.getId()));
        String sql = "select id, name, price, menu_group_id from menu where id = :id";
        Menu found = jdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);

        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(found.getName());
            softly.assertThat(result.getPrice()).isEqualTo(found.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(found.getMenuGroupId());
        });
    }

    @Test
    void 아이디로_메뉴를_조회한다() {
        // given
        Menu expected = new Menu();
        expected.setId(1L);
        expected.setName("후라이드치킨");
        expected.setPrice(BigDecimal.valueOf(16000));
        expected.setMenuGroupId(2L);

        // when
        Menu result = menuDao.findById(expected.getId())
                .orElseThrow();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(expected.getName());
            softly.assertThat(result.getPrice()).isEqualByComparingTo(expected.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
        });
    }

    @Test
    void 메뉴를_전체_조회한다() {
        // given & when
        List<Menu> result = menuDao.findAll();

        // then
        assertThat(result).hasSizeGreaterThan(1);
    }

    @Test
    void 아이디_리스트에_해당하는_메뉴의_개수를_조회한다() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L, 10000L, 10001L);

        // when
        long result = menuDao.countByIdIn(ids);

        // then
        assertThat(result).isEqualTo(3L);
    }
}
