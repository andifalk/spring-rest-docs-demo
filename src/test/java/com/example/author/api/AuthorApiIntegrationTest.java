package com.example.author.api;

import com.example.author.api.resource.CreateAuthorResource;
import com.example.author.boundary.AuthorService;
import com.example.author.entity.Author;
import com.example.author.entity.Gender;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for {@link AuthorRestController authors api}.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthorRestController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets/authors")
public class AuthorApiIntegrationTest {

    private static final String EXPECTED_MEDIA_TYPE = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8";

    private static final String LOCATION_HEADER = "Location";

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused")
    @MockBean
    private AuthorService authorService;

    private ResponseFieldsSnippet authorResponseFieldsSnippet = responseFields(
            fieldWithPath("id").description("The author id"),
            fieldWithPath("firstname").description("The author's first name"),
            fieldWithPath("lastname").description(
                    "The author's last name"),
            fieldWithPath("gender").description(
                    "The author's gender"),
            fieldWithPath("_links").ignored()
    );

    private ResponseFieldsSnippet authorListResponseFieldsSnippet = responseFields(
            fieldWithPath("authors[]").description("List of authors"),
            fieldWithPath("authors[].id").description("The author's id"),
            fieldWithPath("authors[].firstname").description("The author's first name"),
            fieldWithPath("authors[].lastname").description(
                    "The author's last name"),
            fieldWithPath("authors[].gender").description(
                    "The author's gender"),
            fieldWithPath("authors[]._links").ignored(),
            fieldWithPath("_links").ignored()
    );

    private UUID stephenKingId = UUID.randomUUID();
    private UUID bobMartinId = UUID.randomUUID();
    private UUID createdAuthorId = UUID.randomUUID();
    private Author bobMartin;

    @Before
    public void initMocks() {

        Author stephenKing = new Author(stephenKingId, Gender.MALE, "Stephen", "King");
        bobMartin = new Author(bobMartinId, Gender.MALE,"Robert C.", "Martin");


        when(authorService.findAllAuthors()).thenReturn(Arrays.asList(stephenKing, bobMartin));

        when(authorService.findByIdentifier(eq(stephenKingId))).thenReturn(stephenKing);
        when(authorService.findByIdentifier(eq(bobMartinId))).thenReturn(bobMartin);

        when(authorService.findByLastname(eq(bobMartin.getLastname()))).thenReturn(Collections.singletonList(bobMartin));

        when(authorService.createAuthor(any(Author.class))).thenAnswer(invocation -> {
            Author author = invocation.getArgumentAt(0, Author.class);
            ReflectionTestUtils.setField(author, "id", 1L);
            return author;
        });

        when(authorService.deleteAuthor(eq(stephenKingId))).thenReturn(true);
    }


    @Test
    public void documentAndVerifyCreateAuthor() throws Exception {
        String expectedUrl = AuthorRestController.AUTHOR_RESOURCE_PATH + "/" + createdAuthorId;

        CreateAuthorResource createAuthorResource = new CreateAuthorResource(
                createdAuthorId,"Stephen", "Hawking",
                Gender.MALE);

        ConstrainedFields fields = new ConstrainedFields(CreateAuthorResource.class);

        this.mockMvc.perform(post(AuthorRestController.AUTHOR_RESOURCE_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(createAuthorResource))
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION_HEADER, endsWith(expectedUrl)))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.lastname").isString())
                .andDo(
                        document(
                                "document-create-author",
                                requestFields(
                                        fields
                                                .withPath("firstname")
                                                .description("The author's first name")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("lastname")
                                                .description("The author's last name")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("gender")
                                                .description("The author's gender")
                                                .type(JsonFieldType.STRING),
                                        fields
                                                .withPath("identifier")
                                                .description("The author's identifier (generated automatically if not set)")
                                                .type(JsonFieldType.STRING)
                                                .optional()
                                )
                        ))
                .andDo(document("document-create-author", preprocessResponse(prettyPrint()),
                        links(linkWithRel(Link.REL_SELF).description("Links to the created author")),
                        responseHeaders(
                                headerWithName(LOCATION_HEADER)
                                        .description("The location of created author resource")
                        ),
                        authorResponseFieldsSnippet
                ));
    }

    @Test
    public void findByIdentifier() throws Exception {
        this.mockMvc.perform(get(AuthorRestController.AUTHOR_RESOURCE_PATH + "/{id}", stephenKingId.toString())
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(EXPECTED_MEDIA_TYPE))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.lastname").isString())
                .andDo(
                        document("document-get-author",
                                preprocessResponse(prettyPrint()),
                                links(linkWithRel(Link.REL_SELF).description("Links to the current author")),
                                authorResponseFieldsSnippet
                        ));
    }

    @Test
    public void findAllAuthors() throws Exception {
        this.mockMvc.perform(get(AuthorRestController.AUTHOR_RESOURCE_PATH)
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(EXPECTED_MEDIA_TYPE))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors.length()").value(is(2)))
                .andDo(
                        document("document-get-authors",
                                preprocessResponse(prettyPrint()),
                                links(linkWithRel(Link.REL_SELF).description("Links to the current list of authors")),
                                authorListResponseFieldsSnippet
                        ));
    }

    @Test
    public void documentAndVerifyFindByLastname() throws Exception {
        this.mockMvc.perform(get(AuthorRestController.AUTHOR_RESOURCE_PATH
                + "/search?lastname=" + bobMartin.getLastname())
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(EXPECTED_MEDIA_TYPE))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors.length()").value(is(1)))
                .andDo(
                        document("document-find-authors-by-lastname",
                                requestParameters(
                                        parameterWithName("lastname")
                                                .optional()
                                                .description("Search parameter to filter authors for given last name")
                                )
                        ))
                .andDo(
                        document("document-find-authors-by-lastname",
                                preprocessResponse(prettyPrint()),
                                links(linkWithRel(Link.REL_SELF).description("Links to the author list")),
                                authorListResponseFieldsSnippet
                        ));
    }

    @Test
    public void documentAndVerifyDeleteAuthor() throws Exception {
        this.mockMvc.perform(delete(
                AuthorRestController.AUTHOR_RESOURCE_PATH + "/{id}", stephenKingId.toString())
                .accept(MediaType.parseMediaType(EXPECTED_MEDIA_TYPE)))
                .andExpect(status().isNoContent())
                .andDo(
                        document("document-delete-author"));
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