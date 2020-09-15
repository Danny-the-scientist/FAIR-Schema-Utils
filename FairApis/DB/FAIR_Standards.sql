
CREATE TABLE public.project  (
	projectid 	int8 NOT NULL DEFAULT nextval('id_seq'::regclass),
	name      	varchar(25) NULL,
	startdate 	date NULL,
	enddate   	date NULL,
	objectives	text NULL,
	folder_id 	varchar(150) NULL,
	PRIMARY KEY(projectid)
)
GO
ALTER TABLE public.project
	ADD CONSTRAINT unique_project_name
	UNIQUE (name)
GO

CREATE TABLE public.metadata_category  (
	metadatacategoryid	int8 NOT NULL DEFAULT nextval('id_seq'::regclass),
	category          	varchar(100) NULL,
	uiname            	varchar(100) NULL,
	projectid         	int8 NOT NULL,
	description       	text NULL,
	canonical_type    	varchar(40) NULL,
	csvtemplate_name  	varchar(50) NULL,
	categorytype      	varchar(40) NULL DEFAULT NULL::character varying,
	PRIMARY KEY(metadatacategoryid)
)
GO
ALTER TABLE public.metadata_category
	ADD CONSTRAINT metadata_category_category_projectid_key
	UNIQUE (category, projectid)
GO
ALTER TABLE public.metadata_category
	ADD CONSTRAINT met_project_id
	FOREIGN KEY(projectid)
	REFERENCES public.project(projectid)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
GO

CREATE TABLE public.generated_templates  (
	id                	int8 NOT NULL DEFAULT nextval('id_seq'::regclass),
	metadatacategoryid	int8 NOT NULL,
	templateid        	text NOT NULL,
	jsonschema        	text NOT NULL,
	PRIMARY KEY(id)
)
GO
ALTER TABLE public.generated_templates
	ADD CONSTRAINT unique_met_id
	UNIQUE (metadatacategoryid)
GO
ALTER TABLE public.generated_templates
	ADD CONSTRAINT temp_met_id
	FOREIGN KEY(metadatacategoryid)
	REFERENCES public.metadata_category(metadatacategoryid)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
GO

CREATE TABLE public.category_descriptors  (
	categorydescriptorid     	int8 NOT NULL DEFAULT nextval('id_seq'::regclass),
	metadatacategoryid       	int8 NOT NULL,
	fieldname                	varchar(100) NULL,
	fieldpropertyiri         	text NULL,
	description              	text NULL,
	importance               	int8 NOT NULL,
	fieldtype_schema         	varchar(50) NULL,
	ontologyname             	varchar(50) NULL,
	ontologybranch           	varchar(100) NULL,
	ontologyiri              	text NULL,
	controlledvocabulary     	varchar(255) NULL,
	dateloaded               	timestamp NULL DEFAULT now(),
	pittable                 	varchar(50) NULL,
	pitmilestone             	varchar(50) NULL,
	pharosspec               	varchar(50) NULL,
	metadataspec             	varchar(50) NULL,
	fieldcardinality         	varchar(25) NULL,
	dataupdated              	timestamp NULL,
	fieldtype_cedar          	varchar(50) NULL,
	custom_importance        	varchar(25) NULL,
	associated_milestone_link	varchar(50) NULL,
	PRIMARY KEY(categorydescriptorid)
)
GO
ALTER TABLE public.category_descriptors
	ADD CONSTRAINT category_descriptors_fieldname_metadatacategoryid_key
	UNIQUE (fieldname, metadatacategoryid)
GO
ALTER TABLE public.category_descriptors
	ADD CONSTRAINT cat_met_id
	FOREIGN KEY(metadatacategoryid)
	REFERENCES public.metadata_category(metadatacategoryid)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
GO
