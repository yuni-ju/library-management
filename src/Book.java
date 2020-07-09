public class Book {

    private String name, borrowInfo, reserveInfo;
    private int lentCount = 0;

    // index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorrowInfo() {
        return borrowInfo;
    }

    public void setBorrowInfo(String borrowInfo) {
        this.borrowInfo = borrowInfo;
    }

    public String getReserveInfo() {
        return reserveInfo;
    }

    public void setReserveInfo(String reserveInfo) {
        this.reserveInfo = reserveInfo;
    }

    public int getLentCount() {
        return lentCount;
    }

    public void setLentCount(int lentCount) {
        this.lentCount = lentCount;
    }
}
