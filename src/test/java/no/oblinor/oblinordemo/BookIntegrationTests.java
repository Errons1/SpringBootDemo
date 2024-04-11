package no.oblinor.oblinordemo;

import no.oblinor.oblinordemo.book.Book;
import no.oblinor.oblinordemo.book.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository repository;

    private Book book;

    @BeforeEach
    public void beforeEach() {
        book = new Book("Title", 199);
    }

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    }

    @Test
    public void testHelloWorld_returnStatus200() {

        String url = "/api/v1/book/hello-world";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String expected = "Hello World!";

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expects 200 Created from server");
        assertNotNull(response.getBody());
        assertEquals(expected, response.getBody(), "Expects TRUE that body matches");
    }

    @Test
    public void getAllBooks_BooksExist_status200() {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Book tmp = new Book("Title" + i, 199 + i);
            books.add(tmp);
        }
        repository.saveAll(books);
        repository.flush();

        String url = "/api/v1/book/";
        ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(url, Book[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertIterableEquals(books, Arrays.asList(responseEntity.getBody()));
    }

    @Test
    public void getAllAccounts_noBookExist_status200() {
        String url = "/api/v1/book/";
        ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(url, Book[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, Arrays.asList(responseEntity.getBody()).size());
    }

    @Test
    public void getBookById_booksExist_status200() {
        repository.save(book);
        repository.flush();

        String url = "/api/v1/book/" + book.getId();
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity(url, Book.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(book, responseEntity.getBody());
    }

    @Test
    public void getBookById_noBookExist_status404() {
        book.setId(1L);
        String url = "/api/v1/book/" + book.getId();
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity(url, Book.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    
    @Test
    public void createBook_status201() {
        String url = "/api/v1/book/";
        ResponseEntity<Book> responseEntity = restTemplate.postForEntity(url, book, Book.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(book, responseEntity.getBody());
    }

    @Test
    public void createBook_status400() {
        book.setPages(-1);
        String url = "/api/v1/book/";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, book, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
    
    @Test
    public void deleteBook_status200() {
        book = repository.save(book);
        repository.flush();
        
        String url = "/api/v1/book/" + book.getId();
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        assertFalse(repository.existsById(book.getId()));
    }
}
