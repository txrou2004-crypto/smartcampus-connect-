package com.smartcampus.libraryservice.controller;

import com.smartcampus.libraryservice.model.Book;
import com.smartcampus.libraryservice.model.BookReservation;
import com.smartcampus.libraryservice.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final LibraryService service;

    public BookController(LibraryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book created = service.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable String bookId) {
        Book book = service.getBookById(bookId);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{bookId}/availability")
    public ResponseEntity<String> checkAvailability(@PathVariable String bookId) {
        String status = service.checkBookAvailability(bookId);
        if (status.equals("BOOK_NOT_FOUND")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }

    @PostMapping("/{bookId}/borrow/{studentId}")
    public ResponseEntity<BookReservation> borrowBook(@PathVariable String bookId,
                                                      @PathVariable String studentId) {
        BookReservation reservation = service.borrowBook(bookId, studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PutMapping("/return/{reservationId}")
    public ResponseEntity<BookReservation> returnBook(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.returnBook(reservationId));
    }

    @PutMapping("/renew/{reservationId}")
    public ResponseEntity<BookReservation> renewBook(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.renewBook(reservationId));
    }

    @GetMapping("/student/{studentId}/reservations")
    public List<BookReservation> getReservationsByStudent(@PathVariable String studentId) {
        return service.getReservationsByStudent(studentId);
    }
}
