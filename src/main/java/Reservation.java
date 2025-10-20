package main.java;

public class Reservation {
    private String userId;
    private String bookId;
    
    public Reservation(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
