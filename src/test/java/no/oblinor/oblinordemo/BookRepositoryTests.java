package no.oblinor.oblinordemo;

import no.oblinor.oblinordemo.book.Book;
import no.oblinor.oblinordemo.book.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class BookRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository repository;

    private Book book;

    @BeforeEach
    public void beforeEach() {
        book = new Book("Title", 199);
    }

    @AfterEach
    public void afterEach() {
        entityManager.clear();
    }
    
    @Test
    public void deleteById_bookExist() {
        entityManager.persistAndFlush(book);
        
        repository.deleteById(book.getId());
        
        Book deletedBook = entityManager.find(Book.class, book.getId());
        assertNull(deletedBook, "Expect to return null when the book is deleted.");
    }
}
