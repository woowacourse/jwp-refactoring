package kitchenpos.table.domain;

import javax.persistence.Embeddable;

import kitchenpos.order.exception.TableEmptyException;
import kitchenpos.table.dto.InvalidNumberOfGuestException;

@Embeddable
public class TableNumberOfGuests {

    private Integer numberOfGuests;

    public TableNumberOfGuests() {
    }

    public TableNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeGuest(int numberOfGuests, boolean isEmpty) {
        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestException();
        }

        if (isEmpty) {
            throw new TableEmptyException("빈 테이블은 Guest를 변경할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
