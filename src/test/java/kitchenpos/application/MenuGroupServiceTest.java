package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.env.MockEnvironment;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    MenuGroup menuGroup1;
    MenuGroup menuGroup2;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);

        menuGroup1 = new MenuGroup();
        menuGroup1.setName("DD");

        menuGroup2 = new MenuGroup();
        menuGroup2.setId(1L);
        menuGroup2.setName("DD");
    }

    @DisplayName("정상 Menu Group 생성")
    @Test
    void create() {
        when(menuGroupDao.save(menuGroup1)).thenReturn(menuGroup2);

        assertThat(menuGroupService.create(menuGroup1)).isEqualToComparingFieldByField(menuGroup2);
    }

    @DisplayName("정상 find all")
    @Test
    void list() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        assertThat(menuGroupService.list()).usingRecursiveComparison()
            .isEqualTo(Arrays.asList(menuGroup1, menuGroup2));
    }
}
