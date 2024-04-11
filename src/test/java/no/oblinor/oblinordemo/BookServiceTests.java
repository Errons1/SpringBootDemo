package no.oblinor.oblinordemo;

import no.oblinor.oblinordemo.book.Book;
import no.oblinor.oblinordemo.book.BookRepository;
import no.oblinor.oblinordemo.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTests {

    @MockBean
    private BookRepository repository;

    @Autowired
    private BookService service;

    private Book book;

    @BeforeEach
    public void beforeEach() {
        book = new Book("Title", 199);
    }
    
    @Test
    public void getAllBooks_returnsAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Title1", 199));
        books.add(new Book("Title2", 299));
        books.add(new Book("Title3", 399));
    
        when(repository.findAll()).thenReturn(books);
    
        List<Book> result = service.getAllBooks();
    
        assertEquals(3, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Title2", result.get(1).getTitle());
        assertEquals("Title3", result.get(2).getTitle());
    
        verify(repository).findAll();
    }
    
    
    @Test
    public void getBookById_returnsBook() {
        book.setId(1L);
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(book));
        
        Book result = service.getBookById(book.getId());
        
        assertEquals(book, result);
        
        verify(repository).findById(ArgumentMatchers.anyLong());
    }
    
    @Test
    public void createBook_returnsBook() {
        Book createdBook = new Book("New Book", 299);
        when(repository.save(book)).thenReturn(createdBook);
    
        Book result = service.createBook(book);
    
        assertEquals(createdBook, result);
    
        verify(repository).save(book);
    }
    
    @Test
    public void deleteBook_() {
        
    }
}
