package ai.turintech.modelcatalog.rest.support.constants;

/** Profile constants allowed by the apps. */
public interface ApplicationProfiles {

  // Spring profiles for development, test and production, see https://www.jhipster.tech/profiles/
  /** Constant <code>SPRING_PROFILE_DEVELOPMENT="dev"</code> */
  String SPRING_PROFILE_DEVELOPMENT = "dev";
  /** Constant <code>SPRING_PROFILE_PRODUCTION="prod"</code> */
  String SPRING_PROFILE_PRODUCTION = "prod";
  /**
   * Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
   * Constant <code>SPRING_PROFILE_CLOUD="cloud"</code>
   */
  String SPRING_PROFILE_CLOUD = "cloud";
  /**
   * Spring profile used when deploying to Heroku Constant <code>SPRING_PROFILE_HEROKU="heroku"
   * </code>
   */
  String SPRING_PROFILE_HEROKU = "heroku";
  /**
   * Spring profile used to enable OpenAPI doc generation Constant <code>
   * SPRING_PROFILE_API_DOCS="api-docs"</code>
   */
  String SPRING_PROFILE_API_DOCS = "api-docs";
  /**
   * Spring profile used to disable running liquibase Constant <code>
   * SPRING_PROFILE_NO_LIQUIBASE="no-liquibase"</code>
   */
  String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

  /** Spring profile used when Sentry is enabled Constant <code>SENTRY="sentry"</code> */
  String SENTRY = "sentry";
}
