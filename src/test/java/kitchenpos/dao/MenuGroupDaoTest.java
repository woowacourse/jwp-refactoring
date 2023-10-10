package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class MenuGroupDaoTest {

    private MenuGroupDao menuGroupDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private RowMapper<MenuGroup> rowMapper = (rs, rowNum) -> {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(rs.getLong("id"));
        menuGroup.setName(rs.getString("name"));
        return menuGroup;
    };

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(jdbcTemplate.getJdbcTemplate().getDataSource());
    }

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");

        // when
        MenuGroup result = menuGroupDao.save(menuGroup);

        // then
        String sql = "select id, name from menu_group where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of("id", result.getId()));
        MenuGroup found = jdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);

        assertThat(found.getName()).isEqualTo(result.getName());
    }

    @Test
    void 아이디로_메뉴_그룹을_조회한다() {
        // given
        MenuGroup expected = new MenuGroup();
        expected.setId(1L);
        expected.setName("두마리메뉴");

        // when
        MenuGroup result = menuGroupDao.findById(expected.getId())
                .orElseThrow();

        // then
        assertThat(result.getName()).isEqualTo(expected.getName());
    }

    @Test
    void 메뉴_그룹을_전체_조회한다() {
        // given & when
        List<MenuGroup> result = menuGroupDao.findAll();

        // then
        assertThat(result).hasSizeGreaterThan(1);
    }

    @Test
    void 존재하는_아이디로_조회하면_참이다() {
        // given & when
        boolean result = menuGroupDao.existsById(1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 존재하지_않는_아이디로_조회하면_거짓이다() {
        // given & when
        boolean result = menuGroupDao.existsById(100000L);

        // then
        assertThat(result).isFalse();
    }
}
