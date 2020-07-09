public class Member {

    private String id = " ";
    private String name, status, phone , reserveBook;

    // id=8, name=10, status=10, phone=11, reserveBook=10;

    public int ReturnDays(){ return 10; }
    public int BorrowNum() {
        return 3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReserveBook() {
        return reserveBook;
    }

    public void setReserveBook(String reserveBook) {
        this.reserveBook = reserveBook;
    }
}
