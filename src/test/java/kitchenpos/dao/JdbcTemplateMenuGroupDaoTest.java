package kitchenpos.dao;

import static kitchenpos.common.MenuGroupFixtures.MENU_GROUP1_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Nested
    @DisplayName("MenuGroupId에 해당하는 MenuGroup이 존재하는지 판단 시")
    class ExistsById {

        @Test
        @DisplayName("존재하면 true를 반환한다.")
        void exist() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(MENU_GROUP1_REQUEST());

            // when & then
            assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
        }

        @Test
        @DisplayName("존재하면 true를 반환한다.")
        void notExist() {
            // given
            final Long notExistId = -1L;

            // when & then
            assertThat(menuGroupDao.existsById(notExistId)).isFalse();
        }
    }
}
