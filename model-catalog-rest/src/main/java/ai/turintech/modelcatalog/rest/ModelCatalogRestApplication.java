package ai.turintech.modelcatalog.rest;

import ai.turintech.modelcatalog.rest.config.ApplicationProperties;
import ai.turintech.modelcatalog.rest.config.CRLFLogConverter;
import ai.turintech.modelcatalog.rest.support.constants.ApplicationProfiles;
import ai.turintech.modelcatalog.rest.support.profile.DefaultProfileUtil;
import jakarta.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class ModelCatalogRestApplication {

  private static final Logger log = LoggerFactory.getLogger(ModelCatalogRestApplication.class);

  private final Environment env;

  public ModelCatalogRestApplication(Environment env) {
    this.env = env;
  }

  /**
   * Initializes model_catalog.
   *
   * <p>Spring profiles can be configured with a program argument
   * --spring.profiles.active=your-active-profile
   *
   * <p>You can find more information on how profiles work with JHipster on <a
   * href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
   */
  @PostConstruct
  public void initApplication() {
    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    if (activeProfiles.contains(ApplicationProfiles.SPRING_PROFILE_DEVELOPMENT)
        && activeProfiles.contains(ApplicationProfiles.SPRING_PROFILE_PRODUCTION)) {
      log.error(
          "You have misconfigured your application! It should not run "
              + "with both the 'dev' and 'prod' profiles at the same time.");
    }
    if (activeProfiles.contains(ApplicationProfiles.SPRING_PROFILE_DEVELOPMENT)
        && activeProfiles.contains(ApplicationProfiles.SPRING_PROFILE_CLOUD)) {
      log.error(
          "You have misconfigured your application! It should not "
              + "run with both the 'dev' and 'cloud' profiles at the same time.");
    }
  }

  /**
   * Main method, used to run the application.
   *
   * @param args the command line arguments.
   */
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ModelCatalogRestApplication.class);
    DefaultProfileUtil.addDefaultProfile(app);
    Environment env = app.run(args).getEnvironment();
    logApplicationStartup(env);
  }

  private static void logApplicationStartup(Environment env) {
    String protocol =
        Optional.ofNullable(env.getProperty("server.ssl.key-store"))
            .map(key -> "https")
            .orElse("http");
    String applicationName = env.getProperty("spring.application.name");
    String serverPort = env.getProperty("server.port");
    String contextPath =
        Optional.ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
    String hostAddress = "localhost";
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.warn("The host name could not be determined, using `localhost` as fallback");
    }
    log.info(
        CRLFLogConverter.CRLF_SAFE_MARKER,
        "----------------------------------------------------------"
            + "\tApplication '{}' is running! Access URLs:"
            + "\tLocal: \t\t{}://localhost:{}{}"
            + "\tExternal: \t{}://{}:{}{}"
            + "\tProfile(s): \t{}"
            + "----------------------------------------------------------",
        applicationName,
        protocol,
        serverPort,
        contextPath,
        protocol,
        hostAddress,
        serverPort,
        contextPath,
        env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles());

    String configServerStatus = env.getProperty("configserver.status");
    if (configServerStatus == null) {
      configServerStatus = "Not found or not setup for this application";
    }
    log.info(
        CRLFLogConverter.CRLF_SAFE_MARKER,
        "\n----------------------------------------------------------\n\t"
            + "Config Server: \t{}\n----------------------------------------------------------",
        configServerStatus);
  }
}
