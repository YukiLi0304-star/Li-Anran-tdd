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

}
