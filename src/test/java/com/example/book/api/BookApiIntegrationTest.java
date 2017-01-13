package com.example.book.api;

import com.example.book.api.resource.CreateBookResource;
import com.example.book.api.resource.UpdateBookResource;
import com.example.book.boundary.BookService;
import com.example.book.entity.Book;
import com.example.book.entity.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for {@link BookRestController books api}.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BookRestController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets/books")
@WithMockUser
public class BookApiIntegrationTest {

    private static final String EXPECTED_MEDIA_TYPE = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8";

    private static final String LOCATION_HEADER = "Location";

    private static final String IF_MATCH_HEADER = "If-Match";

    private static final String IF_UNMODIFIED_SINCE_HEADER = "If-Unmodified-Since";

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused")
    @MockBean
    private BookService bookService;

    private UUID phoenixBookId = UUID.randomUUID();
    private UUID potterBookId = UUID.randomUUID();
    private UUID createdBookId = UUID.randomUUID();

    private Book phoenix;
    private Book potter;

    private ResponseFieldsSnippet bookResponseFieldsSnippet = responseFields(
            fieldWithPath("id").description("The book id"),
            fieldWithPath("title").description("The book title"),
            fieldWithPath("description").description(
                    "The book description"),
            fieldWithPath("isbn").description(
                    "The ISBN of the book"),
            fieldWithPath("genre").description(
                    "The book genre"),
            fieldWithPath("authors").description(
                    "The book authors"),
            fieldWithPath("_links").ignored()
    );

    private ResponseFieldsSnippet bookListResponseFieldsSnippet = responseFields(
            fieldWithPath("books[]").description("List of books"),
            fieldWithPath("books[].id").description("The book id"),
            fieldWithPath("books[].title").description("The book title"),
            fieldWithPath("books[].description").description(
                    "The book description"),
            fieldWithPath("books[].isbn").description(
                    "The ISBN of the book"),
            fieldWithPath("books[].genre").description(
                    "The book genre"),
            fieldWithPath("books[].authors").description(
                    "The book authors"),
            fieldWithPath("books[]._links").ignored(),
            fieldWithPath("_links").ignored()
    );

    @Before
    public void initMocks() {

        phoenix = new Book(phoenixBookId, "Project Phoenix", "978-0-9587-5175-0",
                "Bill is an IT manager at Parts Unlimited...", Genre.COMPUTER);
        ReflectionTestUtils.setField(phoenix, "version", 1L);
        ReflectionTestUtils.setField(phoenix, "lastModifiedAt", new Date());
        potter = new Book(potterBookId, "Harry Potter and the Cursed Child", "978-0-7515-6535-5",
                "Based on an original new story by J.K. Rowling, John Tiffany and Jack Thorne, ...",
                Genre.COMPUTER);
        ReflectionTestUtils.setField(potter, "version", 1L);
        ReflectionTestUtils.setField(potter, "lastModifiedAt", new Date());

        when(bookService.findAllBooks()).thenReturn(Arrays.asList(phoenix, potter));

        when(bookService.findByIdentifier(eq(phoenixBookId))).thenReturn(phoenix);
        when(bookService.findByIdentifier(eq(potterBookId))).thenReturn(potter);

        when(bookService.findByIsbn(eq(phoenix.getIsbn()))).thenReturn(Collections.singletonList(phoenix));
        when(bookService.findByTitle(eq(potter.getTitle()))).thenReturn(Collections.singletonList(potter));

        when(bookService.createBook(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgumentAt(0, Book.class);
            ReflectionTestUtils.setField(book, "id", 1L);
            ReflectionTestUtils.setField(book, "version", 1L);
            ReflectionTestUtils.setField(book, "lastModifiedAt", new Date());
            return book;
        });

        when(bookService.updateBook(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgumentAt(0, Book.class);
            ReflectionTestUtils.setField(book, "id", 1L);
            ReflectionTestUtils.setField(book, "version", 2L);
            ReflectionTestUtils.setField(book, "lastModifiedAt", new Date());
            return book;
        });

        when(bookService.deleteBook(eq(phoenixBookId))).thenReturn(true);
    }

    @Test
    public void documentAndVerifyCreateBook() throws Exception {
        String expectedUrl = BookRestController.BOOK_RESOURCE_PATH + "/" + createdBookId;

        CreateBookResource createBookResource = new CreateBookResource(
                createdBookId,"Raspberry Pi 3: Beginner to Pro", "978-1-5393-4298-4",
                "Learn all about the Raspberry Pi3 and what you can do with it", Genre.COMPUTER);

        ConstrainedFields fields = new ConstrainedFields(CreateBookResource.class);

        this.mockMvc.perform(post(BookRestController.BOOK_RESOURCE_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(createBookResource))
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION_HEADER, endsWith(expectedUrl)))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.title").isString())
                .andDo(
                        document(
                                "document-create-book",
                                requestFields(
                                        fields
                                                .withPath("title")
                                                .description("The book title")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("isbn")
                                                .description("The book's ISBN number (ISBN-13 format)")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("genre")
                                                .description("The book genre")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("identifier")
                                                .description("The book identifier (generated automatically if not set)")
                                                .type(JsonFieldType.STRING)
                                                .optional(),
                                        fields
                                                .withPath("description")
                                                .description("The book description")
                                                .type(JsonFieldType.STRING)
                                                .optional()
                                )
                        ))
                .andDo(document("document-create-book", preprocessResponse(prettyPrint()),
                        links(linkWithRel(Link.REL_SELF).description("Links to the created book")),
                        responseHeaders(
                                headerWithName(LOCATION_HEADER)
                                        .description("The location of created book resource")
                        ),
                        bookResponseFieldsSnippet
                ));
    }

    @Test
    public void documentAndVerifyUpdateBook() throws Exception {
        UpdateBookResource updateBookResource = new UpdateBookResource("Project Phoenix", "978-0-9587-5175-0",
                "Bill is an IT manager at Parts Unlimited", Genre.COMPUTER);

        ConstrainedFields fields = new ConstrainedFields(UpdateBookResource.class);

        this.mockMvc.perform(put(BookRestController.BOOK_RESOURCE_PATH + "/{id}", phoenixBookId.toString())
                .header(IF_MATCH_HEADER, "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(updateBookResource))
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.title").isString())
                .andDo(
                        document(
                                "document-update-book",
                                requestFields(
                                        fields
                                                .withPath("title")
                                                .description("The book title")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("isbn")
                                                .description("The book's ISBN number (ISBN-13 format)")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("genre")
                                                .description("The book genre")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("identifier")
                                                .description("The book identifier (generated automatically if not set)")
                                                .type(JsonFieldType.STRING)
                                                .optional(),
                                        fields
                                                .withPath("description")
                                                .description("The book description")
                                                .type(JsonFieldType.STRING)
                                                .optional()
                                )
                        ))
                .andDo(document("document-update-book", preprocessResponse(prettyPrint()),
                        links(linkWithRel(Link.REL_SELF).description("Links to the updated book")),
                        responseHeaders(
                                headerWithName("ETag")
                                        .description("The location of created book resource")
                        ),
                        bookResponseFieldsSnippet
                ));
    }

    @Test
    public void documentAndVerifyFindByIdentifier() throws Exception {

        this.mockMvc.perform(get(BookRestController.BOOK_RESOURCE_PATH + "/{id}", phoenixBookId.toString())
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(EXPECTED_MEDIA_TYPE))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.title").isString())
                .andDo(
                        document("document-get-book",
                                preprocessResponse(prettyPrint()),
                                links(linkWithRel(Link.REL_SELF).description("Links to the current book")),
                                bookResponseFieldsSnippet
                        ));
    }

    @Test
    public void documentAndVerifyFindAllBooks() throws Exception {

        this.mockMvc.perform(get(BookRestController.BOOK_RESOURCE_PATH)
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(EXPECTED_MEDIA_TYPE))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(is(2)))
                .andDo(
                        document("document-get-books",
                                preprocessResponse(prettyPrint()),
                                links(linkWithRel(Link.REL_SELF).description("Links to the current list of books")),
                                bookListResponseFieldsSnippet
                        ));
    }

    @Test
    public void documentAndVerifyFindByIsbn() throws Exception {
        this.mockMvc.perform(get(BookRestController.BOOK_RESOURCE_PATH + "/search?isbn=" + phoenix.getIsbn())
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(EXPECTED_MEDIA_TYPE))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(is(1)))
                .andDo(
                        document("document-find-books-by-isbn",
                                requestParameters(
                                        parameterWithName("isbn")
                                                .optional()
                                                .description("Search parameter to filter books for given ISBN"),
                                        parameterWithName("title")
                                                .optional()
                                                .description("Search parameter to filter books for given title")
                                )
                        ))
                .andDo(
                        document("document-find-books-by-isbn",
                                preprocessResponse(prettyPrint()),
                                links(linkWithRel(Link.REL_SELF).description("Links to the book list")),
                                bookListResponseFieldsSnippet
                        ));
    }

    @Test
    public void documentAndVerifyFindByTitle() throws Exception {
        this.mockMvc.perform(get(BookRestController.BOOK_RESOURCE_PATH + "/search?title=" + potter.getTitle())
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(EXPECTED_MEDIA_TYPE))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(is(1)))
                .andDo(
                        document("document-find-books-by-title",
                                requestParameters(
                                        parameterWithName("isbn")
                                                .optional()
                                                .description("Search parameter to filter books for given ISBN"),
                                        parameterWithName("title")
                                                .optional()
                                                .description("Search parameter to filter books for given title")
                                )
                        ))
                .andDo(
                        document("document-find-books-by-title",
                                preprocessResponse(prettyPrint()),
                                links(linkWithRel(Link.REL_SELF).description("Links to the book list")),
                                bookListResponseFieldsSnippet
                        ));
    }

    @Test
    public void documentAndVerifyDeleteBook() throws Exception {
        this.mockMvc.perform(delete(
                BookRestController.BOOK_RESOURCE_PATH + "/{id}", phoenixBookId.toString())
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isNoContent())
                .andDo(
                        document("document-delete-book"));
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }

}