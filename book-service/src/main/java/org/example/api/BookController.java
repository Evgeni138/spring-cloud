package org.example.api;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final Faker faker;
    private final List<Book> books;

    public BookController() {
        this.faker = new Faker();
        final List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 15; i++) {
            Book book = new Book();
            book.setId(UUID.randomUUID());
            book.setName(faker.book().title());

            Author author = new Author();
            author.setId(UUID.randomUUID());
            author.setFirstName(faker.name().firstName());
            author.setLastName(faker.name().lastName());

            book.setAuthor(author);

            books.add(book);
        }

        this.books = List.copyOf(books);
    }

    @GetMapping
    public List<Book> getAll() {
        return books;
    }

    @GetMapping("/random")
    public Book getRandom() {
        final int randomIndex = faker.number().numberBetween(0, books.size());
        return books.get(randomIndex);
    }

    @PostConstruct
    void init() {


    }

}
