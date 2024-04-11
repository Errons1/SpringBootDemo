package no.oblinor.oblinordemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.oblinor.oblinordemo.book.Book;
import no.oblinor.oblinordemo.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    private Book book;

    @BeforeEach
    public void beforeEach() {
        book = new Book("Title", 199);
    }

    @Test
    public void helloWorld_status200() throws Exception {
        String json = new ObjectMapper().writeValueAsString("Hello World!");

        String url = "/api/v1/book/hello-world";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    public void getAllBooks_status200() throws Exception {
        when(service.getAllBooks()).thenReturn(new ArrayList<>());

        String url = "/api/v1/book/";
        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        verify(service).getAllBooks();
    }

    @Test
    public void getAllBooks_emptyList_status200() throws Exception {
        when(service.getAllBooks()).thenReturn(null);

        String url = "/api/v1/book/";
        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        verify(service).getAllBooks();
    }

    @Test
    public void getBookById_status200() throws Exception {
        long id = 1L;
        when(service.getBookById(id))
                .thenReturn(new Book("Title", 199));

        String url = "/api/v1/book/" + id;
        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        verify(service).getBookById(id);
    }

    @Test
    public void getBookById_bookNotExist_status404() throws Exception {
        long id = 1L;
        when(service.getBookById(id)).thenReturn(null);
       
        String url = "/api/v1/book/" + id;
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound());

        verify(service).getBookById(id);
    }

    @Test
    public void createBook_status201() throws Exception {
        when(service.createBook(any(Book.class))).thenReturn(book);

        String json = new ObjectMapper().writeValueAsString(book);
        String url = "/api/v1/book/";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).createBook(any(Book.class));
    }

    @Test
    public void createBook_return400() throws Exception {
        Book book = new Book("Title", -1);

        String json = new ObjectMapper().writeValueAsString(book);
        String url = "/api/v1/book/";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void deleteBook_byId_status200() throws Exception {
        long id = 1L;

        when(service.deleteBookById(id)).thenReturn(true);

        this.mockMvc.perform(delete("/api/v1/book/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).deleteBookById(id);
    }
}
