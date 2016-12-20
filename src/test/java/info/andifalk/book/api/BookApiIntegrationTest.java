package info.andifalk.book.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.andifalk.book.api.resource.CreateBookResource;
import info.andifalk.book.boundary.BookService;
import info.andifalk.book.entity.Book;
import info.andifalk.book.entity.Genre;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for {@link BookRestController books api}.
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringRestDocsDemoApplication.class)
@WebMvcTest(controllers = BookRestController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets/books")
public class BookApiIntegrationTest {

    private static final String EXPECTED_MEDIA_TYPE = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8";

    private static final String LOCATION_HEADER = "Location";

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused")
    @MockBean
    private BookService bookService;

    private UUID phoenixBookId = UUID.randomUUID();

    private UUID potterBookId = UUID.randomUUID();

    @Before
    public void initMocks() {

        Book phoenix = new Book(phoenixBookId, "Project Phoenix", "978-0-9587-5175-0",
                "Bill is an IT manager at Parts Unlimited...", Genre.COMPUTER);

        Book potter = new Book(potterBookId, "Harry Potter and the Cursed Child", "978-0-7515-6535-5",
                "Based on an original new story by J.K. Rowling, John Tiffany and Jack Thorne, ...",
                Genre.COMPUTER);

        when(bookService.findAllBooks()).thenReturn(Arrays.asList(phoenix, potter));

        when(bookService.findByIdentifier(eq(phoenixBookId))).thenReturn(phoenix);
        when(bookService.findByIdentifier(eq(potterBookId))).thenReturn(potter);
    }

    @Test
    public void createBook() throws Exception {
        String expectedUrl = BookRestController.BOOK_RESOURCE_PATH + "/" + COMPANY_ID_1;

        CreateBookResource createBookResource = new CreateBookResource(
                "Raspberry Pi 3: Beginner to Pro", "978-1-5393-4298-4",
                "Learn all about the Raspberry Pi3 and what you can do with it", Genre.COMPUTER);

        this.mockMvc.perform(post(BookRestController.BOOK_RESOURCE_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(createBookResource))
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION_HEADER, endsWith(expectedUrl)))
                .andExpect(jsonPath("$.title").value("Raspberry Pi 3: Beginner to Pro"))
                .andExpect(jsonPath("$.streetAddress.zipCode").value(ADDRESS_ZIPCODE))
                .andExpect(jsonPath("$.streetAddress.city").value(ADDRESS_CITY))
                .andExpect(jsonPath("$.streetAddress.area").value(ADDRESS_AREA))
                .andExpect(jsonPath("$.streetAddress.country").value(ADDRESS_COUNTRY))
                .andExpect(jsonPath("$.streetAddress.street").value(ADDRESS_STREETNAME))
                .andExpect(jsonPath("$.streetAddress.houseNumber").value(ADDRESS_HOUSE_NUMBER))
                .andExpect(jsonPath("$.postBoxAddress.zipCode").value(ADDRESS_ZIPCODE))
                .andExpect(jsonPath("$.postBoxAddress.city").value(ADDRESS_CITY))
                .andExpect(jsonPath("$.postBoxAddress.area").isEmpty())
                .andExpect(jsonPath("$.postBoxAddress.country").value(ADDRESS_COUNTRY))
                .andExpect(jsonPath("$.postBoxAddress.postBox").value(ADDRESS_POBOX))
                .andDo(
                        document(
                                "document-create-company",
                                requestFields(
                                        fieldWithPath("name").description("Company name"),
                                        fieldWithPath("representative").description("UUID of the user representative"),
                                        fieldWithPath("streetAddress").description("The company's street address "
                                                + "(either the street address or the post box address or both "
                                                + "must be provided"),
                                        fieldWithPath("postBoxAddress").description("The company's post office box "
                                                + "address (either the street address or the post box address or both "
                                                + "must be provided")
                                )
                        ))
                .andDo(document("document-create-company", preprocessResponse(prettyPrint()),
                        companyLinks,
                        responseHeaders(
                                headerWithName(LOCATION_HEADER)
                                        .description("The location of created company resource")
                        ),
                        companyResponseFields
                ));
    }

    @Test
    public void findByIdentifier() throws Exception {
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
                                responseFields(
                                        fieldWithPath("id").description("The book id"),
                                        fieldWithPath("title").description("The book title"),
                                        fieldWithPath("description").description(
                                                "The book description"),
                                        fieldWithPath("isbn").description(
                                                "The ISBN of the book"),
                                        fieldWithPath("genre").description(
                                                "The book genre"),
                                        fieldWithPath("_links").ignored()
                                )
                        ));
    }

    @Test
    public void findAllBooks() throws Exception {
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
                                responseFields(
                                        fieldWithPath("books[]").description("List of books"),
                                        fieldWithPath("books[].id").description("The book id"),
                                        fieldWithPath("books[].title").description("The book title"),
                                        fieldWithPath("books[].description").description(
                                                "The book description"),
                                        fieldWithPath("books[].isbn").description(
                                                "The ISBN of the book"),
                                        fieldWithPath("books[].genre").description(
                                                "The book genre"),
                                        fieldWithPath("books[]._links").ignored(),
                                        fieldWithPath("_links").ignored()
                                )
                        ));
    }

    @Test
    public void findByIsbn() throws Exception {

    }

    @Test
    public void deleteBook() throws Exception {

    }

}