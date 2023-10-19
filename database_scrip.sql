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
DROP TABLE IF EXISTS _User;
DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS Sector;
DROP TABLE IF EXISTS Area;
DROP TABLE IF EXISTS State;
DROP TABLE IF EXISTS Profile;
DROP TABLE IF EXISTS Priority;
DROP TABLE IF EXISTS Category;

-------------------------------------------------------SENTENCIAS CREATE-----------------------------------------------------------------
create table _user (userType int4 not null, _user  bigserial not null, blocked boolean not null, deleted timestamp, email varchar(255), firstName varchar(255), lastName varchar(255), password varchar(255), username varchar(255), name varchar(255), language int8, areamanager int8, profile int8, sector int8, sectormanager int8, sectorspecialist int8, primary key (_user));
create table Area (area int8 not null, description varchar(255), primary key (area));
create table Category (category  bigserial not null, description varchar(255), sector int8, primary key (category));
create table Language (language  bigserial not null, code varchar(255), name varchar(255), primary key (language));
create table Priority (priority  bigserial not null, description varchar(255), primary key (priority));
create table Profile (profile  bigserial not null, description varchar(255), primary key (profile));
create table Sector (sector int8 not null, description varchar(255), name varchar(255), area int8, sectormanager int8, primary key (sector));
create table State (state  bigserial not null, description varchar(255), primary key (state));
create table Task (task  bigserial not null, datelimit date, description varchar(255), title varchar(255), category int8, owner int8, parenttask int8, priority int8, solver int8, state int8, primary key (task));
alter table if exists _user add constraint UKnlcolwbx8ujaen5h0u2kr2bn2 unique (username);
alter table if exists _user add constraint FK3w1qaiwi4yiev04506t797vg1 foreign key (language) references Language;
alter table if exists _user add constraint FKrkg36sb95sxe7qh91o3xb8sgh foreign key (areamanager) references Area;
alter table if exists _user add constraint FKrrep94vrbsopf32pkq2j88guv foreign key (profile) references Profile;
alter table if exists _user add constraint FK9csw78ttifar8pjdos1265lo0 foreign key (sector) references Sector;
alter table if exists _user add constraint FKgsy1ybic7ck5kpwq0c5j84cm5 foreign key (sectormanager) references Sector;
alter table if exists _user add constraint FKhtkwbg1up3wxqucisoukgss9h foreign key (sectorspecialist) references Sector;
alter table if exists Sector add constraint FK74nacl8arhbnvgl2g0qr7jpvg foreign key (area) references Area;
alter table if exists Sector add constraint FK3p1xysxqh9wx2e1w5qbfr6s4j foreign key (sectormanager) references _user;
alter table if exists Category add constraint FKmwsq03irmrl6i6tivip9rwgw2 foreign key (sector) references Sector;
alter table if exists Task add constraint FKbony1xrxv1fxsm2lmh8tkv14w foreign key (category) references Category;
alter table if exists Task add constraint FKxeokwbpxofrggic1mtfo2wux foreign key (owner) references _user;
alter table if exists Task add constraint FKk875d0l310sbf5xvgq6kjpp5w foreign key (parenttask) references Task;
alter table if exists Task add constraint FKbeb4907fgeogoexy691duu2df foreign key (priority) references Priority;
alter table if exists Task add constraint FKibyha0ladw0cnyjdk5eup6ln8 foreign key (solver) references _user;
alter table if exists Task add constraint FKpkwokuruyvi5mrum67gfcdmc5 foreign key (state) references State;
------------------------------------------- INSERTS DE ENUMS
INSERT INTO Profile (profile, description) VALUES 
(1, 'USER'),
(2, 'SPECIALIST'),
(3, 'SECTOR MANAGER'),
(4, 'AREA MANAGER');

-- Insertar datos en la tabla 'Area'
INSERT INTO Area (area, description) VALUES 
(1, 'INFORMATIC');

INSERT INTO Sector (sector, name, description, sectormanager, area) VALUES 
(1, 'DEVELOPMENT', 'Development Sector', NULL, 1),
(2, 'COMUNICATION', 'Communication Sector', NULL, 1),
(3, 'SUPPORT', 'Support Sector', NULL, 1);

INSERT INTO Category (category, sector, description) VALUES 
(1, 1, 'DEVELOPMENT REQUEST'),
(2, 1, 'MODIFICATION REQUEST'),
(3, 1, 'ERROR CORRECTION'),
(4, 2, 'INTERNET PROBLEMS'),
(5, 2, 'INTERNAL NETWORK PROBLEMS'),
(6, 2, 'SERVICE ISSUES'),
(9, 3, 'OPERATING SYSTEM HELP'),
(10, 3, 'HARDWARE OPERATION HELP');
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
