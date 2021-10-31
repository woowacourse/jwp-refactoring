package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private static TableGroup tableGroup;

    @Mock
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테스트 이름")
    void create() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("테스트 이름")
    void list() {
        //given

        //when
        List<OrderTable> list = tableService.list();
        //then
        assertThat(list).isNotEmpty();
    }

    @Test
    @DisplayName("테스트 이름")
    void changeEmpty() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("테스트 이름")
    void changeNumberOfGuests() {
        //given

        //when

        //then
    }

    private void addTable() {
        OrderTable orderTable = new OrderTable();

//        tableService.create();
    }
}
