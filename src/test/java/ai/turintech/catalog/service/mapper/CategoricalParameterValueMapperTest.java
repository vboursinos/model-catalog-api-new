package ai.turintech.catalog.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CategoricalParameterValueMapperTest {

    private CategoricalParameterValueMapper categoricalParameterValueMapper;

    @BeforeEach
    public void setUp() {
        categoricalParameterValueMapper = new CategoricalParameterValueMapperImpl();
    }
}
