-- SQLite

DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Destino;
DROP TABLE IF EXISTS Recomendacion;
DROP TABLE IF EXISTS Comentario;
DROP TABLE IF EXISTS Continente;
DROP TABLE IF EXISTS Pais;
DROP TABLE IF EXISTS imgDestino;
DROP TABLE IF EXISTS imgActividad;
DROP TABLE IF EXISTS Actividad;
DROP TABLE IF EXISTS Favoritos;
DROP TABLE IF EXISTS Visitados;
DROP TABLE IF EXISTS Historial;


CREATE TABLE IF NOT EXISTS  Usuario (
  id INTEGER PRIMARY KEY,
  nombre TEXT,
  email TEXT,
  password TEXT,
  salt TEXT,
  tokenSesion TEXT,
  expSesion DATETIME,
  paisOrigen INTEGER,
  metaViajes INTEGER,
  fotoPerfil TEXT
);

CREATE TABLE IF NOT EXISTS  Destino (
  id INTEGER PRIMARY KEY,
  titulo TEXT,
  descripcion TEXT,
  paisId INTEGER,
  numPuntuaciones INTEGER,
  sumaPuntuaciones INTEGER,
  gastoTotal INTEGER,
  diasEstanciaTotal INTEGER,
  indiceSeguridad INTEGER,
  moneda TEXT,
  clima TEXT
);

CREATE TABLE IF NOT EXISTS  Recomendacion (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  actividadId INTEGER,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (actividadId) REFERENCES Actividad(id)
);

CREATE TABLE IF NOT EXISTS  Comentario (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  destinoId INTEGER,
  texto TEXT,
  permisoExtraInfo INTEGER,
  estanciaDias INTEGER,
  dineroGastado INTEGER,
  valoracion INTEGER,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);

CREATE TABLE IF NOT EXISTS Continente (
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS  Pais (
  id INTEGER PRIMARY KEY,
  nombre TEXT UNIQUE,
  continente VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS  imgDestino (
  id INTEGER PRIMARY KEY,
  destinoId INTEGER,
  nombre TEXT UNIQUE,
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);

CREATE TABLE IF NOT EXISTS  imgActividad (
  id INTEGER PRIMARY KEY,
  destinoId INTEGER,
  nombre TEXT UNIQUE,
  FOREIGN KEY (destinoId) REFERENCES Actividad(id)
);

CREATE TABLE IF NOT EXISTS  Actividad (
  id INTEGER PRIMARY KEY,
  titulo TEXT,
  descripcion TEXT,
  numRecomendado INTEGER,
  destinoId INTEGER,
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);

CREATE TABLE IF NOT EXISTS  Favoritos (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  destinoId INTEGER,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);

CREATE TABLE IF NOT EXISTS  Visitados (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  destinoId INTEGER,
  fechaMarcado DATE,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);

CREATE TABLE IF NOT EXISTS  Historial (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  destinoId INTEGER,
  fechaVisita DATETIME,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);


INSERT INTO `Pais` (`nombre`, `continente`) VALUES ('Afganistán', 'Asia'), ('Islas Aland', 'Europe'), ('Albania', 'Europe'), ('Argelia', 'Africa'), ('Samoa Americana', 'Oceania'), ('Andorra', 'Europe'), ('Angola', 'Africa'), ('Anguila', 'North America'), ('Antártida', 'Antarctica'), ('Antigua y Barbuda', 'North America'), ('Argentina', 'South America'), ('Armenia', 'Asia'), ('Aruba', 'North America'), ('Australia', 'Oceania'), ('Austria', 'Europe'), ('Azerbaiyán', 'Asia'), ('Bahamas', 'North America'), ('Bahréin', 'Asia'), ('Bangladesh', 'Asia'), ('Barbados', 'North America'), ('Bielorrusia', 'Europe'), ('Bélgica', 'Europe'), ('Belice', 'North America'), ('Benin', 'Africa'), ('islas Bermudas', 'North America'), ('Bután', 'Asia'), ('Bolivia', 'South America'), ('Bonaire, Sint Eustatius y Saba', 'North America'), ('Bosnia y Herzegovina', 'Europe'), ('Botswana', 'Africa'), ('Isla Bouvet', 'Antarctica'), ('Brasil', 'South America'), ('Territorio Británico del Océano Índico', 'Asia'), ('Brunei Darussalam', 'Asia'), ('Bulgaria', 'Europe'), ('Burkina Faso', 'Africa'), ('Burundi', 'Africa'), ('Camboya', 'Asia'), ('Camerún', 'Africa'), ('Canadá', 'North America'), ('Cabo Verde', 'Africa'), ('Islas Caimán', 'North America'), ('República Centroafricana', 'Africa'), ('Chad', 'Africa'), ('Chile', 'South America'), ('porcelana', 'Asia'), ('Isla de Navidad', 'Asia'), ('Islas Cocos (Keeling)', 'Asia'), ('Colombia', 'South America'), ('Comoras', 'Africa'), ('Congo', 'Africa'), ('Congo, República Democrática del Congo', 'Africa'), ('Islas Cook', 'Oceania'), ('Costa Rica', 'North America'), ('Costa de Marfil', 'Africa'), ('Croacia', 'Europe'), ('Cuba', 'North America'), ('Curazao', 'North America'), ('Chipre', 'Asia'), ('Republica checa', 'Europe'), ('Dinamarca', 'Europe'), ('Djibouti', 'Africa'), ('Dominica', 'North America'), ('República Dominicana', 'North America'), ('Ecuador', 'South America'), ('Egipto', 'Africa'), ('El Salvador', 'North America'), ('Guinea Ecuatorial', 'Africa'), ('Eritrea', 'Africa'), ('Estonia', 'Europe'), ('Etiopía', 'Africa'), ('Islas Falkland (Malvinas)', 'South America'), ('Islas Faroe', 'Europe'), ('Fiyi', 'Oceania'), ('Finlandia', 'Europe'), ('Francia', 'Europe'), ('Guayana Francesa', 'South America'), ('Polinesia francés', 'Oceania'), ('Territorios Franceses del Sur', 'Antarctica'), ('Gabón', 'Africa'), ('Gambia', 'Africa'), ('Georgia', 'Asia'), ('Alemania', 'Europe'), ('Ghana', 'Africa'), ('Gibraltar', 'Europe'), ('Grecia', 'Europe'), ('Groenlandia', 'North America'), ('Granada', 'North America'), ('Guadalupe', 'North America'), ('Guam', 'Oceania'), ('Guatemala', 'North America'), ('Guernsey', 'Europe'), ('Guinea', 'Africa'), ('Guinea-Bissau', 'Africa'), ('Guayana', 'South America'), ('Haití', 'North America'), ('Islas Heard y McDonald', 'Antarctica'), ('Santa Sede (Estado de la Ciudad del Vaticano)', 'Europe'), ('Honduras', 'North America'), ('Hong Kong', 'Asia'), ('Hungría', 'Europe'), ('Islandia', 'Europe'), ('India', 'Asia'), ('Indonesia', 'Asia'), ('Irán (República Islámica de', 'Asia'), ('Irak', 'Asia'), ('Irlanda', 'Europe'), ('Isla del hombre', 'Europe'), ('Israel', 'Asia'), ('Italia', 'Europe'), ('Jamaica', 'North America'), ('Japón', 'Asia'), ('Jersey', 'Europe'), ('Jordán', 'Asia'), ('Kazajstán', 'Asia'), ('Kenia', 'Africa'), ('Kiribati', 'Oceania'), ('Corea del Norte', 'Asia'), ('Corea del Sur', 'Asia'), ('Kosovo', 'Europe'), ('Kuwait', 'Asia'), ('Kirguistán', 'Asia'), ('República Democrática Popular Lao', 'Asia'), ('Letonia', 'Europe'), ('Líbano', 'Asia'), ('Lesoto', 'Africa'), ('Liberia', 'Africa'), ('Jamahiriya Arabe Libia', 'Africa'), ('Liechtenstein', 'Europe'), ('Lituania', 'Europe'), ('Luxemburgo', 'Europe'), ('Macao', 'Asia'), ('Macedonia, la ex República Yugoslava de', 'Europe'), ('Madagascar', 'Africa'), ('Malawi', 'Africa'), ('Malasia', 'Asia'), ('Maldivas', 'Asia'), ('Mali', 'Africa'), ('Malta', 'Europe'), ('Islas Marshall', 'Oceania'), ('Martinica', 'North America'), ('Mauritania', 'Africa'), ('Mauricio', 'Africa'), ('Mayotte', 'Africa'), ('México', 'North America'), ('Micronesia, Estados Federados de', 'Oceania'), ('Moldavia, República de', 'Europe'), ('Mónaco', 'Europe'), ('Mongolia', 'Asia'), ('Montenegro', 'Europe'), ('Montserrat', 'North America'), ('Marruecos', 'Africa'), ('Mozambique', 'Africa'), ('Myanmar', 'Asia'), ('Namibia', 'Africa'), ('Nauru', 'Oceania'), ('Nepal', 'Asia'), ('Países Bajos', 'Europe'), ('Antillas Holandesas', 'North America'), ('Nueva Caledonia', 'Oceania'), ('Nueva Zelanda', 'Oceania'), ('Nicaragua', 'North America'), ('Níger', 'Africa'), ('Nigeria', 'Africa'), ('Niue', 'Oceania'), ('Isla Norfolk', 'Oceania'), ('Islas Marianas del Norte', 'Oceania'), ('Noruega', 'Europe'), ('Omán', 'Asia'), ('Pakistán', 'Asia'), ('Palau', 'Oceania'), ('Territorio Palestino, Ocupado', 'Asia'), ('Panamá', 'North America'), ('Papúa Nueva Guinea', 'Oceania'), ('Paraguay', 'South America'), ('Perú', 'South America'), ('Filipinas', 'Asia'), ('Pitcairn', 'Oceania'), ('Polonia', 'Europe'), ('Portugal', 'Europe'), ('Puerto Rico', 'North America'), ('Katar', 'Asia'), ('Reunión', 'Africa'), ('Rumania', 'Europe'), ('Federación Rusa', 'Asia'), ('Ruanda', 'Africa'), ('San Bartolomé', 'North America'), ('Santa elena', 'Africa'), ('Saint Kitts y Nevis', 'North America'), ('Santa Lucía', 'North America'), ('San Martín', 'North America'), ('San Pedro y Miquelón', 'North America'), ('San Vicente y las Granadinas', 'North America'), ('Samoa', 'Oceania'), ('San Marino', 'Europe'), ('Santo Tomé y Príncipe', 'Africa'), ('Arabia Saudita', 'Asia'), ('Senegal', 'Africa'), ('Serbia', 'Europe'), ('Serbia y Montenegro', 'Europe'), ('Seychelles', 'Africa'), ('Sierra Leona', 'Africa'), ('Singapur', 'Asia'), ('Eslovaquia', 'Europe'), ('Eslovenia', 'Europe'), ('Islas Salomón', 'Oceania'), ('Somalia', 'Africa'), ('Sudáfrica', 'Africa'), ('Georgia del sur y las islas Sandwich del sur', 'Antarctica'), ('Sudán del Sur', 'Africa'), ('España', 'Europe'), ('Sri Lanka', 'Asia'), ('Sudán', 'Africa'), ('Surinam', 'South America'), ('Svalbard y Jan Mayen', 'Europe'), ('Swazilandia', 'Africa'), ('Suecia', 'Europe'), ('Suiza', 'Europe'), ('República Árabe Siria', 'Asia'), ('Taiwan, provincia de China', 'Asia'), ('Tayikistán', 'Asia'), ('Tanzania, República Unida de', 'Africa'), ('Tailandia', 'Asia'), ('Timor-Leste', 'Asia'), ('Para llevar', 'Africa'), ('Tokelau', 'Oceania'), ('Tonga', 'Oceania'), ('Trinidad y Tobago', 'North America'), ('Túnez', 'Africa'), ('Turquía', 'Asia'), ('Turkmenistán', 'Asia'), ('Islas Turcas y Caicos', 'North America'), ('Tuvalu', 'Oceania'), ('Uganda', 'Africa'), ('Ucrania', 'Europe'), ('Emiratos Árabes Unidos', 'Asia'), ('Reino Unido', 'Europe'), ('Estados Unidos', 'North America'), ('Islas menores alejadas de los Estados Unidos', 'North America'), ('Uruguay', 'South America'), ('Uzbekistan', 'Asia'), ('Vanuatu', 'Oceania'), ('Venezuela', 'South America'), ('Vietnam', 'Asia'), ('Islas Vírgenes Británicas', 'North America'), ('Islas Vírgenes, EE. UU.', 'North America'), ('Wallis y Futuna', 'Oceania'), ('Sahara Occidental', 'Africa'), ('Yemen', 'Asia'), ('Zambia', 'Africa'), ('Zimbabue', 'Africa');


-- Insertamos Continentes a la tabla
INSERT INTO Continente (nombre)
SELECT DISTINCT continente FROM Pais;

-- Cambiamos los continentes a claves ajenas de continente
PRAGMA foreign_keys=off;

BEGIN TRANSACTION;

ALTER TABLE Pais RENAME TO Pais_old;

CREATE TABLE Pais (
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(255),
    continente_id INTEGER,
    FOREIGN KEY (continente_id) REFERENCES Continente(id)
);

INSERT INTO Pais (nombre, continente_id)
SELECT p.nombre, c.id 
FROM Pais_old p
JOIN Continente c ON p.continente = c.nombre;

DROP TABLE Pais_old;

COMMIT;

PRAGMA foreign_keys=on;