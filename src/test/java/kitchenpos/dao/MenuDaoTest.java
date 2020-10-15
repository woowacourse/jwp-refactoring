package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MenuDaoTest {
    @Autowired
    private MenuDao menuDao;

    @DisplayName("countByIdIn 기능 테스트")
    @Test
    void countByIdIn() {
        Menu savedMenu1 = menuDao.save(new Menu(null, "a", null, null, null));
        Menu savedMenu2 = menuDao.save(new Menu(null, "b", null, null, null));
        Menu savedMenu3 = menuDao.save(new Menu(null, "c", null, null, null));

        List<Long> ids = Arrays.asList(savedMenu1.getId(), savedMenu2.getId(), 9999L);

        assertThat(menuDao.findAllByIdIn(ids).size()).isEqualTo(2);
    }
}
