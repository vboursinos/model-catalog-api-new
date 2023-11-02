-- Create an extension if not already enabled to generate UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Define a custom function to generate UUIDs as default values
CREATE OR REPLACE FUNCTION generate_uuid()
RETURNS uuid AS $$
BEGIN
  RETURN gen_random_uuid();
END;
$$ LANGUAGE plpgsql;

-- Create the tables with UUIDs as primary keys
CREATE TABLE model_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE ml_task_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL UNIQUE
);

CREATE TABLE model_structure_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL UNIQUE
);

CREATE TABLE model_group_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE model_ensemble_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE model_family_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE model (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  model_type_id uuid REFERENCES model_type (id),
  ml_task_id uuid REFERENCES ml_task_type (id) NOT NULL,
  name varchar NOT NULL UNIQUE,
  display_name varchar NOT NULL,
  structure_id uuid REFERENCES model_structure_type (id) NOT NULL,
  description varchar,
  advantages text[],
  disadvantages text[],
  enabled boolean NOT NULL,
  ensemble_type_id uuid REFERENCES model_ensemble_type (id),
  family_type_id uuid REFERENCES model_family_type (id),
  decision_tree boolean NOT NULL
);

CREATE TABLE rel_model__groups (
  model_id uuid REFERENCES model (id),
  group_id uuid REFERENCES model_group_type (id)
);

CREATE TABLE metric (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL UNIQUE
);

CREATE TABLE rel_model__incompatible_metrics (
  model_id uuid REFERENCES model (id),
  metric_id uuid REFERENCES metric (id)
);

CREATE TABLE parameter (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  model_id uuid REFERENCES model (id),
  name varchar NOT NULL,
  label varchar NOT NULL,
  description varchar,
  enabled boolean NOT NULL,
  fixed_value boolean NOT NULL,
  ordering integer NOT NULL
);

CREATE TABLE parameter_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL UNIQUE
);

CREATE TABLE parameter_distribution_type (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  name varchar NOT NULL UNIQUE
);

CREATE TABLE parameter_type_definition (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  parameter_id uuid REFERENCES parameter (id) NOT NULL,
  parameter_type_id uuid REFERENCES parameter_type (id) NOT NULL,
  parameter_distribution_type_id uuid REFERENCES parameter_distribution_type (id) NOT NULL,
  ordering integer NOT NULL
);

CREATE TABLE categorical_parameter (
  parameter_type_definition_id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
  default_value varchar
);

CREATE TABLE categorical_parameter_value (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  parameter_type_definition_id uuid REFERENCES categorical_parameter (parameter_type_definition_id) NOT NULL,
  value varchar NOT NULL
);

CREATE TABLE integer_parameter (
  parameter_type_definition_id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
  default_value integer
);

CREATE TABLE integer_parameter_value (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  parameter_type_definition_id uuid REFERENCES integer_parameter (parameter_type_definition_id) NOT NULL,
  lower integer NOT NULL,
  upper integer NOT NULL
);

CREATE TABLE float_parameter (
  parameter_type_definition_id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
  default_value double precision
);

CREATE TABLE float_parameter_range (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  parameter_type_definition_id uuid REFERENCES float_parameter (parameter_type_definition_id) NOT NULL,
  is_left_open boolean NOT NULL,
  is_right_open boolean NOT NULL,
  lower double precision NOT NULL,
  upper double precision NOT NULL
);

CREATE TABLE boolean_parameter (
  parameter_type_definition_id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
  default_value boolean
);

CREATE TABLE constraint_edge (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  source_parameter_id uuid REFERENCES parameter (id) NOT NULL,
  target_parameter_id uuid REFERENCES parameter (id) NOT NULL
);

CREATE TABLE mapping (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  constraint_id uuid REFERENCES constraint_edge (id) NOT NULL,
  parameter_type_definition_id uuid REFERENCES parameter_type_definition (id) NOT NULL
);

CREATE TABLE float_constraint_range (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  mapping_id uuid REFERENCES mapping (id) NOT NULL,
  is_left_open boolean NOT NULL,
  is_right_open boolean NOT NULL,
  lower double precision NOT NULL,
  upper double precision NOT NULL
);

CREATE TABLE integer_constraint_range (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  mapping_id uuid REFERENCES mapping (id) NOT NULL,
  lower integer NOT NULL,
  upper integer NOT NULL
);

CREATE TABLE categorical_constraint_value (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  mapping_id uuid REFERENCES mapping (id) NOT NULL,
  value varchar NOT NULL
);

CREATE TABLE boolean_constraint_value (
  id uuid DEFAULT generate_uuid() PRIMARY KEY,
  mapping_id uuid REFERENCES mapping (id) NOT NULL,
  value boolean NOT NULL
);
