create TABLE metric (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE ml_task_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE model_group_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE model_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE model_family_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE model_ensemble_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE model_structure_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE parameter_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

create TABLE parameter_distribution_type (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE dependency_group_type (
  id uuid PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE dependency_type (
  id uuid PRIMARY KEY,
  name varchar NOT NULL,
  dependency_group_id uuid REFERENCES dependency_group_type (id) NOT NULL
);

create TABLE model (
    id uuid PRIMARY KEY,
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
    decision_tree boolean NOT NULL,
    dependency_group_id uuid REFERENCES dependency_group_type (id)
);

create TABLE rel_model__groups (
    model_id uuid REFERENCES model (id),
    group_id uuid REFERENCES model_group_type (id)
);

create TABLE rel_model__incompatible_metrics (
    model_id uuid REFERENCES model (id),
    metric_id uuid REFERENCES metric (id)
);

create TABLE parameter (
  id uuid PRIMARY KEY,
  model_id uuid REFERENCES model (id),
  name varchar NOT NULL,
  label varchar NOT NULL,
  description varchar,
  enabled boolean NOT NULL,
  fixed_value boolean NOT NULL,
  ordering integer NOT NULL
);

create TABLE parameter_type_definition (
  id uuid PRIMARY KEY,
  parameter_id uuid REFERENCES parameter (id) NOT NULL,
  parameter_type_id uuid REFERENCES parameter_type (id) NOT NULL,
  parameter_distribution_type_id uuid REFERENCES parameter_distribution_type (id) NOT NULL,
  ordering integer NOT NULL
);

create TABLE boolean_parameter (
    id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
    default_value boolean
);

create TABLE categorical_parameter (
  id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
  default_value varchar
);

create TABLE integer_parameter (
  id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
  default_value integer
);

create TABLE float_parameter (
  id uuid PRIMARY KEY REFERENCES parameter_type_definition (id),
  default_value double precision
);

CREATE TABLE categorical_parameter_value (
  id uuid PRIMARY KEY,
  parameter_type_definition_id uuid REFERENCES categorical_parameter (id) NOT NULL,
  value varchar NOT NULL
);

CREATE TABLE integer_parameter_value (
  id uuid PRIMARY KEY,
  parameter_type_definition_id uuid REFERENCES integer_parameter (id) NOT NULL,
  lower integer NOT NULL,
  upper integer NOT NULL
);

CREATE TABLE float_parameter_range (
  id uuid PRIMARY KEY,
  parameter_type_definition_id uuid REFERENCES float_parameter (id) NOT NULL,
  is_left_open boolean NOT NULL,
  is_right_open boolean NOT NULL,
  lower double precision NOT NULL,
  upper double precision NOT NULL
);

-- Inserting sample data into the "metrics" table
insert into metric (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d23', 'Metric1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d24', 'Metric2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d25', 'Metric3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d26', 'Metric4');

insert into ml_task_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'mltask1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'mltask2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'mltask3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'mltask4');

insert into model_group_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'modelgroup1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'modelgroup2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'modelgroup3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'modelgroup4');

insert into model_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'modeltype1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'modeltype2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'modeltype3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'modeltype4');

insert into model_family_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'modelfamilytype1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'modelfamilytype2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'modelfamilytype3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'modelfamilytype4');

insert into model_ensemble_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'modelensembletype1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'modelensembletype2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'modelensembletype3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'modelensembletype4');

insert into model_structure_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'modelstructuretype1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'modelstructuretype2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'modelstructuretype3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'modelstructuretype4');

insert into parameter_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'boolean'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'categorcal'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'integer'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'float');

insert into parameter_distribution_type (id, name)
values
    ('1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'parameterdistributiontype1'),
    ('2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'parameterdistributiontype2'),
    ('3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', 'parameterdistributiontype3'),
    ('4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', 'parameterdistributiontype4');

insert into model (id, model_type_id, ml_task_id, name, display_name, structure_id, description, advantages, disadvantages, enabled, ensemble_type_id, family_type_id, decision_tree)
values
  ('123e4567-e89b-12d3-a456-426614174001', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'Model1', 'Display1', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 'Description1', ARRAY['Advantage1'], ARRAY['Disadvantage1'], true, '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', true),
  ('223e4567-e89b-12d3-a456-426614174002', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'Model2', 'Display2', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 'Description2', ARRAY['Advantage2'], ARRAY['Disadvantage2'], true, '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', false);

INSERT INTO parameter (id, model_id, name, label, description, enabled, fixed_value, ordering)
VALUES
  ('523e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174001', 'parameter_name', 'parameter_label', 'parameter_description', true, false, 1),
  ('523e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174001', 'parameter_name2', 'parameter_label2', 'parameter_description2', true, false, 2),
  ('523e4567-e89b-12d3-a456-426614174003', '123e4567-e89b-12d3-a456-426614174001', 'parameter_name3', 'parameter_label3', 'parameter_description3', true, false, 3),
  ('523e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174001', 'parameter_name4', 'parameter_label4', 'parameter_description4', true, false, 4);

insert into parameter_type_definition (id, parameter_id, parameter_type_id, parameter_distribution_type_id, ordering)
values
  ('323e4567-e89b-12d3-a456-426614174001', '523e4567-e89b-12d3-a456-426614174001', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', 1),
  ('323e4567-e89b-12d3-a456-426614174002', '523e4567-e89b-12d3-a456-426614174002', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 2),
  ('323e4567-e89b-12d3-a456-426614174003', '523e4567-e89b-12d3-a456-426614174003', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 2),
  ('323e4567-e89b-12d3-a456-426614174004', '523e4567-e89b-12d3-a456-426614174003', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 2),
  ('323e4567-e89b-12d3-a456-426614174005', '523e4567-e89b-12d3-a456-426614174003', '3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 2),
  ('323e4567-e89b-12d3-a456-426614174006', '523e4567-e89b-12d3-a456-426614174001', '3b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d29', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 2),
  ('323e4567-e89b-12d3-a456-426614174007', '523e4567-e89b-12d3-a456-426614174001', '1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 2),
  ('323e4567-e89b-12d3-a456-426614174008', '523e4567-e89b-12d3-a456-426614174001', '4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21', '2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28', 2);


insert into boolean_parameter (id, default_value)
values
  ('323e4567-e89b-12d3-a456-426614174001', true),
  ('323e4567-e89b-12d3-a456-426614174002', false);

insert into categorical_parameter (id, default_value)
values
  ('323e4567-e89b-12d3-a456-426614174003', 'value1'),
  ('323e4567-e89b-12d3-a456-426614174004', 'value2');

insert into integer_parameter (id, default_value)
values
  ('323e4567-e89b-12d3-a456-426614174005', 10),
  ('323e4567-e89b-12d3-a456-426614174006', 20);

insert into float_parameter (id, default_value)
values
  ('323e4567-e89b-12d3-a456-426614174007', 10.1),
  ('323e4567-e89b-12d3-a456-426614174008', 20.2);

insert into categorical_parameter_value (id, parameter_type_definition_id, value)
values
  ('423e4567-e89b-12d3-a456-426614174001', '323e4567-e89b-12d3-a456-426614174003', 'Category1'),
  ('423e4567-e89b-12d3-a456-426614174002', '323e4567-e89b-12d3-a456-426614174003', 'Category2'),
  ('423e4567-e89b-12d3-a456-426614174003', '323e4567-e89b-12d3-a456-426614174004', 'Category3'),
  ('423e4567-e89b-12d3-a456-426614174004', '323e4567-e89b-12d3-a456-426614174004', 'Category4');

insert into integer_parameter_value (id, parameter_type_definition_id, lower, upper)
values
  ('423e4567-e89b-12d3-a456-426614174001', '323e4567-e89b-12d3-a456-426614174005', 10, 20),
  ('423e4567-e89b-12d3-a456-426614174002', '323e4567-e89b-12d3-a456-426614174005', 5, 15),
  ('423e4567-e89b-12d3-a456-426614174003', '323e4567-e89b-12d3-a456-426614174006', 30, 40),
  ('423e4567-e89b-12d3-a456-426614174004', '323e4567-e89b-12d3-a456-426614174006', 25, 35);

insert into float_parameter_range (id, parameter_type_definition_id, is_left_open, is_right_open, lower, upper)
values
  ('423e4567-e89b-12d3-a456-426614174001', '323e4567-e89b-12d3-a456-426614174007', true, false, 10.5, 20.5),
  ('423e4567-e89b-12d3-a456-426614174002', '323e4567-e89b-12d3-a456-426614174007', false, true, 5.2, 15.7),
  ('423e4567-e89b-12d3-a456-426614174003', '323e4567-e89b-12d3-a456-426614174008', false, false, 30.0, 40.0),
  ('423e4567-e89b-12d3-a456-426614174004', '323e4567-e89b-12d3-a456-426614174008', true, true, 25.3, 35.8);
