package main.java;

import java.util.List;
import java.util.*;
public class ReservationService {

    private final IBookRepository bookRepo;
    private final IReservationRepository reservationRepo;
    private final IUserRepository userRepo;
    private final Map<String, Queue<String>> waitingLists = new HashMap<>();

    public ReservationService(IBookRepository bookRepo, IReservationRepository reservationRepo,IUserRepository userRepo) {
        this.bookRepo = bookRepo;
        this.reservationRepo = reservationRepo;
        this.userRepo = userRepo;
    }

    /**
     * Reserve a book for a user.
     * Throws IllegalArgumentException if book not found.
     * Throws IllegalStateException if no copies available or user already reserved.
     */
    public void reserve(String userId, String bookId) {
        // TODO: Implement using TDD 
        User user = userRepo.findById(userId);
        Book book = bookRepo.findById(bookId);

        if (book == null) {
            throw new IllegalArgumentException("Not found: " + bookId);
        }
        
        if (book.getCopiesAvailable() <= 0) {
            if (user != null && user.isPriority()) {
                waitingLists.computeIfAbsent(bookId, k -> new LinkedList<>()).offer(userId);
                return;
            }else{
                throw new IllegalStateException("Not copies available!");
            }
        }

        if(reservationRepo.existsByUserAndBook(userId, bookId)){
            throw new IllegalStateException("UserAlreadyReserved" + bookId);
        }

        Reservation reservation = new Reservation(userId, bookId);
        reservationRepo.save(reservation);

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepo.save(book);
    }

    /**
     * Cancel an existing reservation for a user.
     * Throws IllegalArgumentException if no such reservation exists.
     */
    public void cancel(String userId, String bookId) {
        // TODO: Implement using TDD
        if (!reservationRepo.existsByUserAndBook(userId, bookId)) {
            throw new IllegalArgumentException("No Reservation");
        }

        reservationRepo.delete(userId, bookId);
        
        Book book = bookRepo.findById(bookId);
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        bookRepo.save(book);

    }

    /**
     * List all active reservations for a given user.
     */
    public List<Reservation> listReservations(String userId) {
        // TODO: Implement using TDD
        return reservationRepo.findByUser(userId);
    }

    /**
     * list all reservations for a book.
     */
    public List<Reservation> listReservationsForBook(String bookId) {
        // TODO: Implement using TDD
        return reservationRepo.findByBook(bookId);
    }
}
