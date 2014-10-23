-- You can use this file to load seed data into the database using SQL statements
--insert into Member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')

-- You can use this file to create a dummy bp database
CREATE USER 'bpuser'@'%' IDENTIFIED BY 'bpuser';
create database bp_rds_db;
grant all on bp_rds_db.* to 'bpuser'@'%' IDENTIFIED BY 'bpuser';
use bp_rds_db;
drop table DerivativeProduct;
drop table BalmoProductCode;
drop table FormulaLine;
drop table FormulaPrice;

create table DerivativeProduct (
   id bigint not null auto_increment,        
   type varchar(50),        
   name varchar(255),        
   product_code bigint,        
   formula_line bigint,          
   formula_price bigint,          
   primary key (id)    
   );
   
create table BalmoProductCode (
   id bigint not null auto_increment,        
   type varchar(50),        
   name varchar(255),       
   primary key (id)    
   );
   
create table FormulaLine (
   id bigint not null auto_increment,        
   type varchar(50),        
   name varchar(255),          
   primary key (id)    
   ); 
   
create table FormulaPrice (
   id bigint not null auto_increment,        
   price bigint(50),        
   name varchar(255),          
   primary key (id)    
   );  

alter table DerivativeProduct 
    ADD CONSTRAINT product_code_fk
    foreign key (product_code) 
    references BalmoProductCode (id);   
	
alter table DerivativeProduct 
    ADD CONSTRAINT formula_line_fk
    foreign key (formula_line) 
    references FormulaLine (id);   
	
alter table DerivativeProduct 
    ADD CONSTRAINT formula_price_fk
    foreign key (formula_price) 
    references FormulaPrice (id);   

