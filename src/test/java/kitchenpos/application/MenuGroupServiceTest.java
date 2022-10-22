package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuGroupService sut;

    @Test
    void delegateSaveAndReturnSavedEntity() {
        // given
        MenuGroup expected = new MenuGroup();
        expected.setName("추천메뉴");

        // when
        MenuGroup actual = sut.create(expected);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void returnAllSavedEntities() {
        // given
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("추천메뉴");
        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("치킨");
        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        // when
        List<MenuGroup> actual = sut.list();

        // then
        assertThat(actual).isNotNull();
    }
}
