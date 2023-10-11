package kitchenpos.dao;

import static kitchenpos.common.MenuFixtures.MENU1_REQUEST;
import static kitchenpos.common.MenuFixtures.MENU1_MENU_GROUP_ID;
import static kitchenpos.common.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.MenuFixtures.MENU1_PRICE;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @Test
    @DisplayName("Menu를 영속화한다.")
    void saveMenu() {
        // given
        final Menu menu = MENU1_REQUEST();

        // when
        Menu savedMenu = menuDao.save(menu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedMenu.getId()).isNotNull();
            softly.assertThat(savedMenu.getName()).isEqualTo(MENU1_NAME);
            softly.assertThat(savedMenu.getPrice()).isEqualTo(MENU1_PRICE);
            softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(MENU1_MENU_GROUP_ID);
        });
    }
}
