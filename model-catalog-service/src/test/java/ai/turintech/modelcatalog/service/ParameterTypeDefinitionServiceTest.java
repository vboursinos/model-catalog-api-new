package ai.turintech.modelcatalog.service;

import static org.mockito.Mockito.when;

import ai.turintech.components.mapper.api.MapperInterface;
import ai.turintech.modelcatalog.dto.ParameterDistributionTypeDTO;
import ai.turintech.modelcatalog.dto.ParameterTypeDTO;
import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import ai.turintech.modelcatalog.dtoentitymapper.ParameterTypeDefinitionMapper;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import ai.turintech.modelcatalog.repository.ParameterTypeDefinitionRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@ExtendWith({MockitoExtension.class})
public class ParameterTypeDefinitionServiceTest extends TestServiceConfig {

  private static final String EXISTING_PARAMETER_TYPE_DEFINITION_ID =
      "323e4567-e89b-12d3-a456-426614174001";

  private static final String PARAMETER_DISTRIBUTION_TYPE_ID =
      "1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27";
  private static final String PARAMETER_TYPE_ID = "1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27";

  @Mock private ApplicationContext context;
  @Mock private ParameterTypeDefinitionRepository repository;

  @Mock
  private MapperInterface<ParameterTypeDefinitionDTO, ParameterTypeDefinition> mapperInterface;

  @Mock private ParameterTypeDefinitionMapper parameterTypeDefinitionMapper;
  @InjectMocks private ParameterTypeDefinitionServiceImpl parameterTypeDefinitionServiceImpl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    ReflectionTestUtils.setField(parameterTypeDefinitionServiceImpl, "context", context);
    ReflectionTestUtils.setField(
        parameterTypeDefinitionServiceImpl, "jdbcScheduler", Schedulers.immediate());
    ReflectionTestUtils.setField(parameterTypeDefinitionServiceImpl, "repository", repository);
    ReflectionTestUtils.setField(
        parameterTypeDefinitionServiceImpl, "mapperInterface", mapperInterface);
  }

  private ParameterTypeDefinitionDTO getSavedParameterTypeDefinitionDTO() {
    ParameterDistributionTypeDTO parameterDistributionTypeDTO = new ParameterDistributionTypeDTO();
    parameterDistributionTypeDTO.setId(UUID.fromString(PARAMETER_DISTRIBUTION_TYPE_ID));
    parameterDistributionTypeDTO.setName("parameterdistributiontype1");

    ParameterTypeDTO parameterTypeDTO = new ParameterTypeDTO();
    parameterTypeDTO.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterTypeDTO.setName("parametertype1");

    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = new ParameterTypeDefinitionDTO();
    parameterTypeDefinitionDTO.setId(UUID.fromString(EXISTING_PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinitionDTO.setDistribution(parameterDistributionTypeDTO);
    parameterTypeDefinitionDTO.setType(parameterTypeDTO);
    parameterTypeDefinitionDTO.setOrdering(13);

    return parameterTypeDefinitionDTO;
  }

  private ParameterTypeDefinition getSavedParameterTypeDefinition() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(EXISTING_PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinition.setOrdering(13);

    return parameterTypeDefinition;
  }

  @Test
  void testFindAllStream() {
    List<ParameterTypeDefinition> mockParameterTypeDefinitions =
        Arrays.asList(getSavedParameterTypeDefinition());

    List<ParameterTypeDefinitionDTO> mockParameterTypeDefinitionDTOList =
        Arrays.asList(getSavedParameterTypeDefinitionDTO());

    when(repository.findAll()).thenReturn(mockParameterTypeDefinitions);

    when(parameterTypeDefinitionMapper.to(mockParameterTypeDefinitions.get(0)))
        .thenReturn(mockParameterTypeDefinitionDTOList.get(0));

    Flux<ParameterTypeDefinitionDTO> result = parameterTypeDefinitionServiceImpl.findAllStream();

    StepVerifier.create(result)
        .expectNextCount(mockParameterTypeDefinitions.size())
        .verifyComplete();
  }

}
