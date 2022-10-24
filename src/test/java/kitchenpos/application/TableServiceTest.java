package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.Fixtures;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    TableService tableService;

    @Autowired
    OrderTableDao orderTableDao;

    @DisplayName("테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable 테이블_1 = Fixtures.테이블_1();

        OrderTable saved = tableService.create(테이블_1);

        assertThat(tableService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }

    @DisplayName("테이블의 빈 상태 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable 테이블_1 = tableService.create(Fixtures.테이블_1());

        테이블_1.setEmpty(false);
        tableService.changeEmpty(테이블_1.getId(), 테이블_1);

        assertThat(orderTableDao.findById(테이블_1.getId()).orElseThrow().isEmpty())
                .isFalse();
    }

    @Test
    void changeNumberOfGuests() {
        OrderTable 테이블_1 = tableService.create(Fixtures.테이블_1());

        테이블_1.setNumberOfGuests(100);
        tableService.changeNumberOfGuests(테이블_1.getId(), 테이블_1);

        assertThat(orderTableDao.findById(테이블_1.getId()).orElseThrow().getNumberOfGuests())
                .isEqualTo(100);
    }
}
