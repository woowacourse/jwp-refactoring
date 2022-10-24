package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(JdbcTemplateMenuGroupDao.class)
public class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @DisplayName("menuGroup을 저장한다.")
    @Test
    void save() {
        // given
        final MenuGroup menugroup = new MenuGroup("신메뉴");

        // when
        final MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menugroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }
}
