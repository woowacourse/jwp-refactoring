package kitchenpos.tablegroup.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfGuests;
    private boolean empty;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeToEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeToEmpty() {
        this.tableGroup = null;
        this.empty = true;
    }

    public void changeToUse() {
        if (!this.empty || this.tableGroup != null) {
            throw new IllegalArgumentException("이미 사용중인 테이블 입니다.");
        }
        this.empty = false;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0 || this.empty) {
            throw new IllegalArgumentException("변경할 손님수는 0명 이상의 정수로 입력해주세요.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
