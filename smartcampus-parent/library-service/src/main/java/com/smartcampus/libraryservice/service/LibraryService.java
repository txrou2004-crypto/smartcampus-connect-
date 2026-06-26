package com.smartcampus.libraryservice.service;

import com.smartcampus.libraryservice.client.StudentClient;
import com.smartcampus.libraryservice.model.*;
import com.smartcampus.libraryservice.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LibraryService {

    private final StudentClient studentClient;
    private final BookRepository bookRepository;
    private final BookReservationRepository bookReservationRepository;
    private final RoomRepository roomRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final ReentrantLock bookLock;
    private final ReentrantLock roomLock;

    public LibraryService(StudentClient studentClient,
                          BookRepository bookRepository,
                          BookReservationRepository bookReservationRepository,
                          RoomRepository roomRepository,
                          RoomReservationRepository roomReservationRepository,
                          ReentrantLock bookLock,
                          ReentrantLock roomLock) {
        this.studentClient = studentClient;
        this.bookRepository = bookRepository;
        this.bookReservationRepository = bookReservationRepository;
        this.roomRepository = roomRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.bookLock = bookLock;
        this.roomLock = roomLock;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book getBookById(String bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public String checkBookAvailability(String bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            return "BOOK_NOT_FOUND";
        }
        return book.getAvailabilityStatus();
    }

    public BookReservation borrowBook(String bookId, String studentId) {
        bookLock.lock();
        try {
            if (!studentClient.isStudentActive(studentId)) {
                throw new RuntimeException("Student is inactive or does not exist");
            }

            Book book = getBookById(bookId);

            if (book == null) {
                throw new RuntimeException("Book not found");
            }

            if ("BORROWED".equalsIgnoreCase(book.getAvailabilityStatus())) {
                throw new RuntimeException("Book is already borrowed");
            }

            book.setAvailabilityStatus("BORROWED");
            bookRepository.save(book);

            BookReservation reservation = new BookReservation();
            reservation.setBook(book);
            reservation.setStudentId(studentId);
            reservation.setStatus("ACTIVE");

            return bookReservationRepository.save(reservation);
        } finally {
            bookLock.unlock();
        }
    }

    public BookReservation returnBook(Integer reservationId) {
        BookReservation reservation = bookReservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Book reservation not found"));

        reservation.setStatus("COMPLETED");

        Book book = reservation.getBook();
        book.setAvailabilityStatus("AVAILABLE");
        bookRepository.save(book);

        return bookReservationRepository.save(reservation);
    }

    public BookReservation renewBook(Integer reservationId) {
        BookReservation reservation = bookReservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Book reservation not found"));

        if (!"ACTIVE".equalsIgnoreCase(reservation.getStatus())) {
            throw new RuntimeException("Only active borrowed books can be renewed");
        }

        return bookReservationRepository.save(reservation);
    }

    public List<BookReservation> getReservationsByStudent(String studentId) {
        return bookReservationRepository.findByStudentId(studentId);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<RoomReservation> getAllRoomReservations() {
        return roomReservationRepository.findAll();
    }

    public RoomReservation reserveRoom(String roomId, String studentId, RoomReservation request) {
        roomLock.lock();
        try {
            if (!studentClient.isStudentActive(studentId)) {
                throw new RuntimeException("Student is inactive or does not exist");
            }

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            if ("BOOKED".equalsIgnoreCase(room.getAvailabilityStatus())) {
                throw new RuntimeException("Room is already booked");
            }

            RoomReservation reservation = new RoomReservation();
            reservation.setRoom(room);
            reservation.setStudentId(studentId);
            reservation.setReservationDate(request.getReservationDate());
            reservation.setStartTime(request.getStartTime());
            reservation.setEndTime(request.getEndTime());
            reservation.setStatus("ACTIVE");

            room.setAvailabilityStatus("BOOKED");
            roomRepository.save(room);

            return roomReservationRepository.save(reservation);
        } finally {
            roomLock.unlock();
        }
    }

    public RoomReservation cancelRoomReservation(Integer reservationId) {
        RoomReservation reservation = roomReservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Room reservation not found"));

        reservation.setStatus("CANCELLED");

        Room room = reservation.getRoom();
        room.setAvailabilityStatus("AVAILABLE");
        roomRepository.save(room);

        return roomReservationRepository.save(reservation);
    }

    public RoomReservation modifyRoomReservation(Integer reservationId, RoomReservation request) {
        RoomReservation reservation = roomReservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Room reservation not found"));

        reservation.setReservationDate(request.getReservationDate());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        return roomReservationRepository.save(reservation);
    }
}