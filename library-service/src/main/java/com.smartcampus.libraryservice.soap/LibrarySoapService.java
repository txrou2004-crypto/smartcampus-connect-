package com.smartcampus.libraryservice.soap;

import com.smartcampus.libraryservice.service.LibraryService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.springframework.stereotype.Component;

@WebService
@Component
public class LibrarySoapService {

    private final LibraryService libraryService;

    public LibrarySoapService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @WebMethod
    public String checkBookAvailability(String bookId) {

        String status = libraryService.checkBookAvailability(bookId);

        if ("BOOK_NOT_FOUND".equals(status)) {
            throw new RuntimeException("SOAP Fault: Book not found");
        }

        return status;
    }
}
