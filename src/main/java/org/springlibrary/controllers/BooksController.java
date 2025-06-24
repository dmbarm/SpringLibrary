package org.springlibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springlibrary.dtos.books.BookResponseDTO;
import org.springlibrary.dtos.books.CreateBookRequestDTO;
import org.springlibrary.dtos.books.UpdateBookRequestDTO;
import org.springlibrary.services.BooksService;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(booksService.getAllBooks());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable long id) {
        return ResponseEntity.ok(booksService.getById(id));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BookResponseDTO> getByTitle(@PathVariable String title) {
        return ResponseEntity.ok(booksService.getByTitle(title));
    }


    @PostMapping()
    public ResponseEntity<Long> addBook(@Valid @RequestBody CreateBookRequestDTO dto,
                                        UriComponentsBuilder uriBuilder) {
        long id = booksService.addBook(dto);

        URI uri = uriBuilder.path("/books/{id}").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).body(id);
    }

    @PatchMapping()
    public ResponseEntity<Void> updateBook(@Valid @RequestBody UpdateBookRequestDTO dto) {
        booksService.updateBook(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        booksService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/title/{title}")
    public ResponseEntity<Void> deleteByTitle(@PathVariable String title) {
        booksService.deleteByTitle(title);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("id/{id}/image")
    public ResponseEntity<Resource> downloadImage(@PathVariable("id") long bookId) {
        GridFsResource resource = booksService.getImage(bookId);

        String filename = resource.getFilename();
        String encodedFilename = (filename != null)
                ? UriUtils.encode(filename, StandardCharsets.UTF_8)
                : "book_cover";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

    @PostMapping("id/{id}/image")
    public ResponseEntity<Void> uploadImage(@PathVariable("id") long bookId, @RequestParam("book_cover") MultipartFile file) {
        booksService.addImage(bookId, file);
        return ResponseEntity.noContent().build();
    }
}