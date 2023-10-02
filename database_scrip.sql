-- Eliminar restricciones de clave externa
ALTER TABLE IF EXISTS Task DROP CONSTRAINT IF EXISTS FKbony1xrxv1fxsm2lmh8tkv14w;
ALTER TABLE IF EXISTS Task DROP CONSTRAINT IF EXISTS FKi74qp8u6bl2nx15dwmsscjh6x;
ALTER TABLE IF EXISTS Task DROP CONSTRAINT IF EXISTS FKk875d0l310sbf5xvgq6kjpp5w;
ALTER TABLE IF EXISTS Task DROP CONSTRAINT IF EXISTS FKbeb4907fgeogoexy691duu2df;
ALTER TABLE IF EXISTS Task DROP CONSTRAINT IF EXISTS FK3m38bdp0thait4na9e3020u9s;
ALTER TABLE IF EXISTS Task DROP CONSTRAINT IF EXISTS FKkoa484dopwpr70kqg6l38ija9;
ALTER TABLE IF EXISTS Task DROP CONSTRAINT IF EXISTS FKpkwokuruyvi5mrum67gfcdmc5;
ALTER TABLE IF EXISTS _User DROP CONSTRAINT IF EXISTS FKggsykdwxj3ecgo9wg6tkjgo36;
ALTER TABLE IF EXISTS Sector DROP CONSTRAINT IF EXISTS FKkgsne2vcnrgru49pse62tu3va;
-- Eliminar tablas
DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS Area;
DROP TABLE IF EXISTS Sector;
DROP TABLE IF EXISTS State;
DROP TABLE IF EXISTS Profile;
DROP TABLE IF EXISTS Priority;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS _User;
-------------------------------------------------------SENTENCIAS CREATE-----------------------------------------------------------------
create table _User (_user int8 not null, name varchar(255), password varchar(255), username varchar(255), areamanager int8, profile int8, sector int8, sectormanager int8, sectorspecialist int8, primary key (_user));
create table Area (area int8 not null, description varchar(255), primary key (area));
create table Category (category int8 not null, description varchar(255), primary key (category));
create table Priority (priority int8 not null, description varchar(255), primary key (priority));
create table Profile (profile int8 not null, description varchar(255), primary key (profile));
create table Sector (sector int8 not null, description varchar(255), name varchar(255), area int8, sectormanager int8, primary key (sector));
create table State (state int8 not null, description varchar(255), primary key (state));
create table Task (task int8 not null, datelimit date, description varchar(255), title varchar(255), category int8, owner int8, parenttask int8, priority int8, sector int8, solver int8, state int8, primary key (task));
alter table if exists _User add constraint FKch177bi2oq6f429g2f75yclaw foreign key (areamanager) references Area;
alter table if exists _User add constraint FKggsykdwxj3ecgo9wg6tkjgo36 foreign key (profile) references Profile;
alter table if exists _User add constraint FKs8y0nah0yquoamouqo6pllpu5 foreign key (sector) references Sector;
alter table if exists _User add constraint FKawr8b9nicr39gtrigigjeq11l foreign key (sectormanager) references Sector;
alter table if exists _User add constraint FKsqlndj0wm88hgq641jryyyidl foreign key (sectorspecialist) references Sector;
alter table if exists Sector add constraint FK74nacl8arhbnvgl2g0qr7jpvg foreign key (area) references Area;
alter table if exists Sector add constraint FKkgsne2vcnrgru49pse62tu3va foreign key (sectormanager) references _User;
alter table if exists Task add constraint FKbony1xrxv1fxsm2lmh8tkv14w foreign key (category) references Category;
alter table if exists Task add constraint FKi74qp8u6bl2nx15dwmsscjh6x foreign key (owner) references _User;
alter table if exists Task add constraint FKk875d0l310sbf5xvgq6kjpp5w foreign key (parenttask) references Task;
alter table if exists Task add constraint FKbeb4907fgeogoexy691duu2df foreign key (priority) references Priority;
alter table if exists Task add constraint FK3m38bdp0thait4na9e3020u9s foreign key (sector) references Sector;
alter table if exists Task add constraint FKkoa484dopwpr70kqg6l38ija9 foreign key (solver) references _User;
alter table if exists Task add constraint FKpkwokuruyvi5mrum67gfcdmc5 foreign key (state) references State;
------------------------------------------- INSERTS DE ENUMS
INSERT INTO Profile (profile, description) VALUES 
(1, 'USER'),
(2, 'SPECIALIST'),
(3, 'SECTOR MANAGER'),
(4, 'AREA MANAGER');
INSERT INTO Category (category, description) VALUES 
(1, 'DEVELOPMENT REQUEST'),
(2, 'MODIFICATION REQUEST'),
(3, 'ERROR CORRECTION'),
(4, 'INTERNET PROBLEMS'),
(5, 'INTERNAL NETWORK PROBLEMS'),
(6, 'SERVICE ISSUES'),
(7, 'EMAIL SERVICE'),
(8, 'DATABASE SERVICE'),
(9, 'OPERATING SYSTEM HELP'),
(10, 'HARDWARE OPERATION HELP');
INSERT INTO Priority (priority, description) VALUES 
(1, 'HIGH'),
(2, 'MEDIUM'),
(3, 'LOW');
INSERT INTO State (state, description) VALUES 
(1, 'SIN ASIGNAR'),
(2, 'POR REALIZAR'),
(3, 'EN PROCESO'),
(4, 'FINALIZADO'),
(5, 'CANCELADO');
-- Insertar datos en la tabla 'Area'
INSERT INTO Area (area, description) VALUES 
(1, 'INFORMATIC');

INSERT INTO Sector (sector, name, description, sectormanager, area) VALUES 
(1, 'DEVELOPMENT', 'Development Sector', NULL, 1),
(2, 'COMUNICATION', 'Communication Sector', NULL, 1),
(3, 'SUPPORT', 'Support Sector', NULL, 1);
INSERT INTO _User (_user, name, password, username, areamanager, profile, sector, sectormanager, sectorspecialist)
VALUES 
(1, 'USER', 'USER', 'USER', NULL, 1, NULL, NULL, NULL),
(2, 'SPECIALIST', 'SPECIALIST', 'SPECIALIST', NULL, 2, 1, NULL, 1),
(3, 'SECTORMANAGER', 'SECTORMANAGER', 'SECTORMANAGER', NULL, 3, 2, 2, NULL),
(4, 'AREAMANAGER', 'AREAMANAGER', 'AREAMANAGER', 1, 4, 3, NULL, NULL);




