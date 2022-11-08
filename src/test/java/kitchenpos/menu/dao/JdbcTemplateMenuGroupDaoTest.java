package kitchenpos.menu.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(JdbcTemplateMenuGroupDao.class)
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @DisplayName("id가 null이면 false를 반환한다.")
    @Test
    void existsById() {
        final boolean result = menuGroupDao.existsById(null);

        assertThat(result).isFalse();
    }
}
