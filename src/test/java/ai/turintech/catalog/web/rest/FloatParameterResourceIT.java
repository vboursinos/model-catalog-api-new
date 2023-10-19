package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.FloatParameter;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.FloatParameterRepository;
import ai.turintech.catalog.service.dto.FloatParameterDTO;
import ai.turintech.catalog.service.mapper.FloatParameterMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link FloatParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FloatParameterResourceIT {

    private static final Double DEFAULT_DEFAULT_VALUE = 1D;
    private static final Double UPDATED_DEFAULT_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/float-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FloatParameterRepository floatParameterRepository;

    @Autowired
    private FloatParameterMapper floatParameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FloatParameter floatParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloatParameter createEntity(EntityManager em) {
        FloatParameter floatParameter = new FloatParameter().defaultValue(DEFAULT_DEFAULT_VALUE);
        return floatParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloatParameter createUpdatedEntity(EntityManager em) {
        FloatParameter floatParameter = new FloatParameter().defaultValue(UPDATED_DEFAULT_VALUE);
        return floatParameter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FloatParameter.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        floatParameter = createEntity(em);
    }

    @Test
    void createFloatParameter() throws Exception {
        int databaseSizeBeforeCreate = floatParameterRepository.findAll().collectList().block().size();
        // Create the FloatParameter
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeCreate + 1);
        FloatParameter testFloatParameter = floatParameterList.get(floatParameterList.size() - 1);
        assertThat(testFloatParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void createFloatParameterWithExistingId() throws Exception {
        // Create the FloatParameter with an existing ID
        floatParameter.setId(1L);
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);

        int databaseSizeBeforeCreate = floatParameterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFloatParametersAsStream() {
        // Initialize the database
        floatParameterRepository.save(floatParameter).block();

        List<FloatParameter> floatParameterList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FloatParameterDTO.class)
            .getResponseBody()
            .map(floatParameterMapper::toEntity)
            .filter(floatParameter::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(floatParameterList).isNotNull();
        assertThat(floatParameterList).hasSize(1);
        FloatParameter testFloatParameter = floatParameterList.get(0);
        assertThat(testFloatParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void getAllFloatParameters() {
        // Initialize the database
        floatParameterRepository.save(floatParameter).block();

        // Get all the floatParameterList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(floatParameter.getId().intValue()))
            .jsonPath("$.[*].defaultValue")
            .value(hasItem(DEFAULT_DEFAULT_VALUE.doubleValue()));
    }

    @Test
    void getFloatParameter() {
        // Initialize the database
        floatParameterRepository.save(floatParameter).block();

        // Get the floatParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, floatParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(floatParameter.getId().intValue()))
            .jsonPath("$.defaultValue")
            .value(is(DEFAULT_DEFAULT_VALUE.doubleValue()));
    }

    @Test
    void getNonExistingFloatParameter() {
        // Get the floatParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFloatParameter() throws Exception {
        // Initialize the database
        floatParameterRepository.save(floatParameter).block();

        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();

        // Update the floatParameter
        FloatParameter updatedFloatParameter = floatParameterRepository.findById(floatParameter.getId()).block();
        updatedFloatParameter.defaultValue(UPDATED_DEFAULT_VALUE);
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(updatedFloatParameter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, floatParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
        FloatParameter testFloatParameter = floatParameterList.get(floatParameterList.size() - 1);
        assertThat(testFloatParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void putNonExistingFloatParameter() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();
        floatParameter.setId(count.incrementAndGet());

        // Create the FloatParameter
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, floatParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFloatParameter() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();
        floatParameter.setId(count.incrementAndGet());

        // Create the FloatParameter
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFloatParameter() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();
        floatParameter.setId(count.incrementAndGet());

        // Create the FloatParameter
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFloatParameterWithPatch() throws Exception {
        // Initialize the database
        floatParameterRepository.save(floatParameter).block();

        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();

        // Update the floatParameter using partial update
        FloatParameter partialUpdatedFloatParameter = new FloatParameter();
        partialUpdatedFloatParameter.setId(floatParameter.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFloatParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFloatParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
        FloatParameter testFloatParameter = floatParameterList.get(floatParameterList.size() - 1);
        assertThat(testFloatParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void fullUpdateFloatParameterWithPatch() throws Exception {
        // Initialize the database
        floatParameterRepository.save(floatParameter).block();

        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();

        // Update the floatParameter using partial update
        FloatParameter partialUpdatedFloatParameter = new FloatParameter();
        partialUpdatedFloatParameter.setId(floatParameter.getId());

        partialUpdatedFloatParameter.defaultValue(UPDATED_DEFAULT_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFloatParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFloatParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
        FloatParameter testFloatParameter = floatParameterList.get(floatParameterList.size() - 1);
        assertThat(testFloatParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void patchNonExistingFloatParameter() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();
        floatParameter.setId(count.incrementAndGet());

        // Create the FloatParameter
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, floatParameterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFloatParameter() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();
        floatParameter.setId(count.incrementAndGet());

        // Create the FloatParameter
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFloatParameter() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRepository.findAll().collectList().block().size();
        floatParameter.setId(count.incrementAndGet());

        // Create the FloatParameter
        FloatParameterDTO floatParameterDTO = floatParameterMapper.toDto(floatParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FloatParameter in the database
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFloatParameter() {
        // Initialize the database
        floatParameterRepository.save(floatParameter).block();

        int databaseSizeBeforeDelete = floatParameterRepository.findAll().collectList().block().size();

        // Delete the floatParameter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, floatParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FloatParameter> floatParameterList = floatParameterRepository.findAll().collectList().block();
        assertThat(floatParameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
