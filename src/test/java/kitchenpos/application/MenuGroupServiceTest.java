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

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService sut;

    @Test
    void delegateSaveAndReturnSavedEntity() {
        // given
        MenuGroup expected = new MenuGroup();
        expected.setId(1L);
        expected.setName("추천메뉴");

        given(menuGroupDao.save(expected)).willReturn(expected);

        // when
        MenuGroup actual = sut.create(expected);

        // then
        assertThat(actual).isEqualTo(expected);
        verify(menuGroupDao, times(1)).save(expected);
    }

    @Test
    void returnAllSavedEntities() {
        // given
        ArrayList<MenuGroup> expected = new ArrayList<>();
        expected.add(new MenuGroup());
        expected.add(new MenuGroup());

        given(menuGroupDao.findAll()).willReturn(expected);

        // when
        List<MenuGroup> actual = sut.list();

        // then
        assertThat(actual).isEqualTo(expected);
        verify(menuGroupDao, times(1)).findAll();
    }
}
