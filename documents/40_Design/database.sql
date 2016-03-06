-- Avocado Share schema generation commands

-- Create users with password set to a secret password.
-- keiM2h0ciOgw;4)-/x-4nvRU5BgD#Y7N

CREATE ROLE avocado_tomcat WITH ENCRYPTED PASSWORD '77eb2c2e52824f26bd47f6d0bc6e1dcb' NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT NOLOGIN;

CREATE SCHEMA avocado_share;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA avocado_share TO avocado_tomcat;

SET search_path TO avocado_share;

CREATE TABLE access_control
(
  id INTEGER NOT NULL,
  creation_date timestamp NOT NULL,
  CONSTRAINT pk_access_control PRIMARY KEY (id)
);

CREATE TABLE file
(
  id INTEGER NOT NULL,
  Title VARCHAR(50) NOT NULL,
  description VARCHAR(512) NOT NULL,
  LastChanged timestamp NOT NULL,
  CONSTRAINT pk_File PRIMARY KEY(id),
  CONSTRAINT fk_File FOREIGN KEY(id) REFERENCES access_control(id)
);

CREATE TABLE file_version
(
  id INTEGER NOT NULL,
  type VARCHAR(32) NOT NULL,
  filename VARCHAR(32) NOT NULL,
  creation_date timestamp NOT NULL,
  CONSTRAINT pk_file_version PRIMARY KEY(id, filename),
  CONSTRAINT fk_file_version FOREIGN KEY(id) REFERENCES file(id)
);

CREATE TABLE access_group
(
  id INTEGER NOT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(512) NOT NULL,
  CONSTRAINT pk_access_group PRIMARY KEY(id),
  CONSTRAINT fk_access_group_id FOREIGN KEY(id) REFERENCES access_control(id),
  CONSTRAINT uc_access_group UNIQUE(name)
);

CREATE TABLE module
(
  id INTEGER NOT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(512) NOT NULL,
  CONSTRAINT pk_module PRIMARY KEY(id),
  CONSTRAINT fk_module_id FOREIGN KEY(id) REFERENCES access_control(id),
  CONSTRAINT uc_module UNIQUE(name)
);

CREATE TABLE identity
(
  id INTEGER NOT NULL,
  prename VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  avatar VARCHAR(65) NOT NULL,
  description VARCHAR(512) NOT NULL,
  password VARCHAR(65) NOT NULL,
  CONSTRAINT pk_identity PRIMARY KEY(id),
  CONSTRAINT fk_identity_id FOREIGN KEY(id) REFERENCES access_control(id)
);


CREATE TABLE ownership
(
  owner_id INTEGER NOT NULL,
  object_id INTEGER NOT NULL,
  CONSTRAINT fk_ownership_owner_id FOREIGN KEY(owner_id) REFERENCES access_control(id),
  CONSTRAINT fk_ownership_objectid FOREIGN KEY(object_id) REFERENCES access_control(id),
  CONSTRAINT pk_ownership PRIMARY KEY(object_id),
  CONSTRAINT uc_ownership UNIQUE(object_id, owner_id)
);

CREATE TABLE email
(
  identity_id INTEGER NOT NULL,
  address VARCHAR(128) NOT NULL,
  verified BOOLEAN NOT NULL,
  CONSTRAINT pk_email PRIMARY KEY(identity_id, address),
  CONSTRAINT fk_email_id FOREIGN KEY(identity_id) REFERENCES identity(id)
);

CREATE TABLE email_verification
(
  identity_id INTEGER NOT NULL,
  address VARCHAR(128) NOT NULL,
  expiry timestamp NOT NULL,
  verification_code VARCHAR(256) NOT NULL,
  CONSTRAINT fk_email_verification FOREIGN KEY(identity_id, address) REFERENCES email(identity_id, address),
  CONSTRAINT pk_email_verification PRIMARY KEY(identity_id, address, verification_code)
);


CREATE TABLE category
(
  object_id INTEGER NOT NULL,
  name VARCHAR(100) NOT NULL,
  CONSTRAINT pk_Category PRIMARY KEY(object_id, name),
  CONSTRAINT fk_Category_object_id FOREIGN KEY (object_id) REFERENCES access_control(id)
);


CREATE TABLE access_level
(
  Level INTEGER NOT NULL,
  Readable BOOLEAN NOT NULL,
  Writable BOOLEAN NOT NULL,
  Manageable BOOLEAN NOT NULL,
  CONSTRAINT pk_access_level PRIMARY KEY (Level),
  CONSTRAINT uc_access_level UNIQUE (Readable, Writable, Manageable)
);

CREATE TABLE rights
(
  object_id INTEGER NOT NULL,
  owner_id INTEGER NOT NULL,
  Level INTEGER NOT NULL,
  CONSTRAINT fk_rights_Level FOREIGN KEY(Level) REFERENCES access_level(Level),
  CONSTRAINT fk_rights_owner_id FOREIGN KEY(owner_id) REFERENCES access_control(id),
  CONSTRAINT fk_rights_object_id FOREIGN KEY(object_id) REFERENCES access_control(id),
  CONSTRAINT pk_rights PRIMARY KEY(object_id, owner_id)
);


CREATE TABLE default_access
(
  object_id INTEGER NOT NULL,
  Level INTEGER NOT NULL,
  CONSTRAINT fk_default_access_object_id FOREIGN KEY(object_id) REFERENCES access_control(id),
  CONSTRAINT fk_default_access_Level FOREIGN KEY(Level) REFERENCES access_level(level),
  CONSTRAINT pk_default_access PRIMARY KEY(object_id)
);


CREATE TABLE rating
(
  object_id INTEGER NOT NULL,
  identity_id INTEGER NOT NULL,
  rating INTEGER NOT NULL,
  CONSTRAINT fk_rating_object_id FOREIGN KEY(object_id) REFERENCES access_control(id),
  CONSTRAINT fk_rating_identity_id FOREIGN KEY(identity_id) REFERENCES identity(id),
  CONSTRAINT pk_rating PRIMARY KEY(object_id, identity_id),
  CONSTRAINT chk_rating CHECK (rating >= 0 AND Rating <= 5)
);

CREATE TABLE uploaded_into
(
  file_id INTEGER NOT NULL,
  module_id INTEGER NOT NULL,
  Level INTEGER NOT NULL,
  CONSTRAINT fk_uploaded_into_file_id FOREIGN KEY(file_id) REFERENCES File(id),
  CONSTRAINT fk_uploaded_into_module_id FOREIGN KEY(module_id) REFERENCES Module(id),
  CONSTRAINT pk_uploaded_into PRIMARY KEY(file_id),
  CONSTRAINT uc_uploaded_into UNIQUE(file_id, module_id)
);
