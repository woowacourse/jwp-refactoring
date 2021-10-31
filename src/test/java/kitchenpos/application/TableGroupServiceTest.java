package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @Mock
    JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @Mock
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테스트 이름")
    void create() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("테스트 이름")
    void ungroup() {
        //given

        //when

        //then
    }
}
