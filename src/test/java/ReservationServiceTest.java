package test.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.*;

import static org.junit.jupiter.api.Assertions.*;
public class ReservationServiceTest {
    private ReservationService reservationService;
    private IBookRepository bookRepo;
    private IReservationRepository reservationRepo;
    
    @BeforeEach
    void setUp() {
        bookRepo = new MemoryBookRepository();
        reservationRepo = new MemoryReservationRepository();
        reservationService = new ReservationService(bookRepo, reservationRepo);
    }

    @Test
    void reserveBookAvailableSuccess(){

        Book book = new Book("001", "Book1", 4);
        bookRepo.save(book);
    
        reservationService.reserve("001", "001");
        boolean exists = reservationRepo.existsByUserAndBook("001", "001");
        assertTrue(exists);
    
    }

    @Test
    void reserveDecreaseBookCopies() {
        Book book = new Book("001", "Book1", 4);
        bookRepo.save(book);
 
        reservationService.reserve("001", "001");
        Book updatedBook = bookRepo.findById("001");
        assertEquals(3, updatedBook.getCopiesAvailable());
    }

    @Test
    void reserveNoBook(){
        assertThrows(IllegalArgumentException.class, () -> {
        reservationService.reserve("001", "No Book");
        });
    }

    @Test
    void reserveNoCopiesAvailable(){
        Book book = new Book("001", "Book1", 0);
        bookRepo.save(book);

        assertThrows(IllegalStateException.class, () -> {
            reservationService.reserve("001", "001");
        });
    }

    @Test
    void reserveUserAlreadyReserved(){
        Book book = new Book("001", "Book1", 4);
        bookRepo.save(book);
        reservationService.reserve("001", "001");

        assertThrows(IllegalStateException.class, () -> {
            reservationService.reserve("001", "001");
        });
    }
}
