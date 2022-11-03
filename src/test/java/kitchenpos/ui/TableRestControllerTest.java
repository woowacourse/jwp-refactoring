package kitchenpos.ui;

import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.ui.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class TableRestControllerTest {

    @Autowired
    private TableRestController tableRestController;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("테이블 등록")
    @Test
    void create() {
        final var request = new OrderTableRequest(2, true);
        final var response = tableRestController.create(request);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation()).isNotNull()
        );
    }

    @DisplayName("전체 테이블 목록 조회")
    @Test
    void list() {
        final var response = tableRestController.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("테이블 비어있는지 여부 수정")
    @Test
    void changeEmpty() {
        final var table = orderTableDao.save(notEmptyTable(2));

        final var request = new OrderTableRequest(2, true);
        final var response = tableRestController.changeEmpty(table.getId(), request);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블 인원 수정")
    @Test
    void changeNumberOfGuests() {
        final var table = orderTableDao.save(notEmptyTable(2));

        final var request = new OrderTableRequest(3, false);
        final var response = tableRestController.changeNumberOfGuests(table.getId(), request);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getNumberOfGuests()).isEqualTo(3)
        );
    }
}
