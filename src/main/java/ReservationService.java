package main.java;

import java.util.List;
import java.util.*;
public class ReservationService {

    private final IBookRepository bookRepo;
    private final IReservationRepository reservationRepo;
    private final IUserRepository userRepo;
    //1.Used DeepSeek AI for code optimization suggestions during Part C development
    //Instruction: “Help me create a WaitingList entity class, IWaitingListRepository interface, and MemoryWaitingListRepository to implement priority users joining the waiting queue”
    //AI will provide an answer, and at the end, recommend alternative optimization methods such as using 'private final Map<String, Queue<String>> waitingLists = new HashMap<>();' in ReservationService to avoid creating additional classes
    private final Map<String, Queue<String>> waitingLists = new HashMap<>();

    //2.Used AI to help resolve some code error issues encountered during development
    //Instruction: “Please help me check why this is giving an error”
    //I will provide AI with a screenshot of my code error (this doesn't mean I'll consult AI for every single error - only when I've reviewed and attempted to solve it myself but still find it difficult to resolve). For such challenging issues, I choose to ask AI for help by providing instructions, and it will analyse why the error occurred and suggest solutions.

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

        Queue<String> waitingQueue = waitingLists.get(bookId);
        if (waitingQueue != null && !waitingQueue.isEmpty()) {
            String nextUserId = waitingQueue.poll();
            Reservation reservation = new Reservation(nextUserId, bookId);
            reservationRepo.save(reservation);
            book.setCopiesAvailable(book.getCopiesAvailable() - 1);
            bookRepo.save(book);
            if (waitingQueue.isEmpty()) {
                waitingLists.remove(bookId);
            }
        }
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
