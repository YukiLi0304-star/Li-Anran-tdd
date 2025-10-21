package test.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
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

    //Cancel test
    @Test
    void cancelExistingReservationIncreaseCopies(){
        Book book = new Book("001", "Book1", 4);
        bookRepo.save(book);
        reservationService.reserve("001", "001");
        reservationService.cancel("001", "001");

        Book updatedBook = bookRepo.findById("001");
        assertEquals(4, updatedBook.getCopiesAvailable());
    }

    @Test
    void cancelNoReservationExists(){
        Book book = new Book("001", "Book1", 4);
        bookRepo.save(book);

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.cancel("001", "001");
        });
    }

    @Test
    void listrReservationsForGivenUser(){
        Book book = new Book("001", "Book1", 4);
        Book book1 = new Book("002", "Book2", 4);
        bookRepo.save(book);
        bookRepo.save(book1);
        reservationService.reserve("001", "001");
        reservationService.reserve("001", "002");

        List<Reservation> reservations = reservationService.listReservations("001");
        assertEquals(2, reservations.size());
    }

    @Test
    void listAllReservationsForABook(){
        Book book = new Book("001", "Book1", 4);
        bookRepo.save(book);
        reservationService.reserve("001", "001");
        reservationService.reserve("002", "001");

        List<Reservation> reservations =reservationService.listReservationsForBook("001");
        assertEquals(2, reservations.size());
    }

    @Test
    void reserveTheLastCopy(){
        Book book = new Book("001", "Book1", 1);
        bookRepo.save(book);
        reservationService.reserve("001", "001");

        Book updatedBook = bookRepo.findById("001");
        assertEquals(0, updatedBook.getCopiesAvailable());
        assertTrue(reservationRepo.existsByUserAndBook("001","001"));
    }
}
