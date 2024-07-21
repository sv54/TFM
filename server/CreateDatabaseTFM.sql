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

CREATE TABLE IF NOT EXISTS Continente (
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS  Pais (
  id INTEGER PRIMARY KEY,
  nombre TEXT UNIQUE,
  iso VARCHAR(255),
  continente VARCHAR(255)
);

INSERT INTO `Pais` (`nombre`, `iso`, `continente`) VALUES
('Afganistán', 'AF', 'Asia'),
('Islas Aland', 'AX', 'Europe'),
('Albania', 'AL', 'Europe'),
('Argelia', 'DZ', 'Africa'),
('Samoa Americana', 'AS', 'Oceania'),
('Andorra', 'AD', 'Europe'),
('Angola', 'AO', 'Africa'),
('Anguila', 'AI', 'North America'),
('Antártida', 'AQ', 'Antarctica'),
('Antigua y Barbuda', 'AG', 'North America'),
('Argentina', 'AR', 'South America'),
('Armenia', 'AM', 'Asia'),
('Aruba', 'AW', 'North America'),
('Australia', 'AU', 'Oceania'),
('Austria', 'AT', 'Europe'),
('Azerbaiyán', 'AZ', 'Asia'),
('Bahamas', 'BS', 'North America'),
('Bahréin', 'BH', 'Asia'),
('Bangladesh', 'BD', 'Asia'),
('Barbados', 'BB', 'North America'),
('Bielorrusia', 'BY', 'Europe'),
('Bélgica', 'BE', 'Europe'),
('Belice', 'BZ', 'North America'),
('Benin', 'BJ', 'Africa'),
('Islas Bermudas', 'BM', 'North America'),
('Bután', 'BT', 'Asia'),
('Bolivia', 'BO', 'South America'),
('Bonaire, Sint Eustatius y Saba', 'BQ', 'North America'),
('Bosnia y Herzegovina', 'BA', 'Europe'),
('Botswana', 'BW', 'Africa'),
('Isla Bouvet', 'BV', 'Antarctica'),
('Brasil', 'BR', 'South America'),
('Territorio Británico del Océano Índico', 'IO', 'Asia'),
('Brunei Darussalam', 'BN', 'Asia'),
('Bulgaria', 'BG', 'Europe'),
('Burkina Faso', 'BF', 'Africa'),
('Burundi', 'BI', 'Africa'),
('Camboya', 'KH', 'Asia'),
('Camerún', 'CM', 'Africa'),
('Canadá', 'CA', 'North America'),
('Cabo Verde', 'CV', 'Africa'),
('Islas Caimán', 'KY', 'North America'),
('República Centroafricana', 'CF', 'Africa'),
('Chad', 'TD', 'Africa'),
('Chile', 'CL', 'South America'),
('China', 'CN', 'Asia'),
('Isla de Navidad', 'CX', 'Asia'),
('Islas Cocos (Keeling)', 'CC', 'Asia'),
('Colombia', 'CO', 'South America'),
('Comoras', 'KM', 'Africa'),
('Congo', 'CG', 'Africa'),
('Congo, República Democrática del Congo', 'CD', 'Africa'),
('Islas Cook', 'CK', 'Oceania'),
('Costa Rica', 'CR', 'North America'),
('Costa de Marfil', 'CI', 'Africa'),
('Croacia', 'HR', 'Europe'),
('Cuba', 'CU', 'North America'),
('Curazao', 'CW', 'North America'),
('Chipre', 'CY', 'Asia'),
('República Checa', 'CZ', 'Europe'),
('Dinamarca', 'DK', 'Europe'),
('Djibouti', 'DJ', 'Africa'),
('Dominica', 'DM', 'North America'),
('República Dominicana', 'DO', 'North America'),
('Ecuador', 'EC', 'South America'),
('Egipto', 'EG', 'Africa'),
('El Salvador', 'SV', 'North America'),
('Guinea Ecuatorial', 'GQ', 'Africa'),
('Eritrea', 'ER', 'Africa'),
('Estonia', 'EE', 'Europe'),
('Etiopía', 'ET', 'Africa'),
('Islas Falkland (Malvinas)', 'FK', 'South America'),
('Islas Faroe', 'FO', 'Europe'),
('Fiyi', 'FJ', 'Oceania'),
('Finlandia', 'FI', 'Europe'),
('Francia', 'FR', 'Europe'),
('Guayana Francesa', 'GF', 'South America'),
('Polinesia Francesa', 'PF', 'Oceania'),
('Territorios Franceses del Sur', 'TF', 'Antarctica'),
('Gabón', 'GA', 'Africa'),
('Gambia', 'GM', 'Africa'),
('Georgia', 'GE', 'Asia'),
('Alemania', 'DE', 'Europe'),
('Ghana', 'GH', 'Africa'),
('Gibraltar', 'GI', 'Europe'),
('Grecia', 'GR', 'Europe'),
('Groenlandia', 'GL', 'North America'),
('Granada', 'GD', 'North America'),
('Guadalupe', 'GP', 'North America'),
('Guam', 'GU', 'Oceania'),
('Guatemala', 'GT', 'North America'),
('Guernsey', 'GG', 'Europe'),
('Guinea', 'GN', 'Africa'),
('Guinea-Bissau', 'GW', 'Africa'),
('Guyana', 'GY', 'South America'),
('Haití', 'HT', 'North America'),
('Islas Heard y McDonald', 'HM', 'Antarctica'),
('Santa Sede (Estado de la Ciudad del Vaticano)', 'VA', 'Europe'),
('Honduras', 'HN', 'North America'),
('Hong Kong', 'HK', 'Asia'),
('Hungría', 'HU', 'Europe'),
('Islandia', 'IS', 'Europe'),
('India', 'IN', 'Asia'),
('Indonesia', 'ID', 'Asia'),
('Irán (República Islámica de)', 'IR', 'Asia'),
('Irak', 'IQ', 'Asia'),
('Irlanda', 'IE', 'Europe'),
('Isla del Hombre', 'IM', 'Europe'),
('Israel', 'IL', 'Asia'),
('Italia', 'IT', 'Europe'),
('Jamaica', 'JM', 'North America'),
('Japón', 'JP', 'Asia'),
('Jersey', 'JE', 'Europe'),
('Jordán', 'JO', 'Asia'),
('Kazajstán', 'KZ', 'Asia'),
('Kenia', 'KE', 'Africa'),
('Kiribati', 'KI', 'Oceania'),
('Corea del Norte', 'KP', 'Asia'),
('Corea del Sur', 'KR', 'Asia'),
('Kosovo', 'XK', 'Europe'),
('Kuwait', 'KW', 'Asia'),
('Kirguistán', 'KG', 'Asia'),
('República Democrática Popular Lao', 'LA', 'Asia'),
('Letonia', 'LV', 'Europe'),
('Líbano', 'LB', 'Asia'),
('Lesoto', 'LS', 'Africa'),
('Liberia', 'LR', 'Africa'),
('Jamahiriya Árabe Libia', 'LY', 'Africa'),
('Liechtenstein', 'LI', 'Europe'),
('Lituania', 'LT', 'Europe'),
('Luxemburgo', 'LU', 'Europe'),
('Macao', 'MO', 'Asia'),
('Macedonia, la ex República Yugoslava de', 'MK', 'Europe'),
('Madagascar', 'MG', 'Africa'),
('Malawi', 'MW', 'Africa'),
('Malasia', 'MY', 'Asia'),
('Maldivas', 'MV', 'Asia'),
('Mali', 'ML', 'Africa'),
('Malta', 'MT', 'Europe'),
('Islas Marshall', 'MH', 'Oceania'),
('Martinica', 'MQ', 'North America'),
('Mauritania', 'MR', 'Africa'),
('Mauricio', 'MU', 'Africa'),
('Mayotte', 'YT', 'Africa'),
('México', 'MX', 'North America'),
('Micronesia, Estados Federados de', 'FM', 'Oceania'),
('Moldavia, República de', 'MD', 'Europe'),
('Mónaco', 'MC', 'Europe'),
('Mongolia', 'MN', 'Asia'),
('Montenegro', 'ME', 'Europe'),
('Montserrat', 'MS', 'North America'),
('Marruecos', 'MA', 'Africa'),
('Mozambique', 'MZ', 'Africa'),
('Myanmar', 'MM', 'Asia'),
('Namibia', 'NA', 'Africa'),
('Nauru', 'NR', 'Oceania'),
('Nepal', 'NP', 'Asia'),
('Países Bajos', 'NL', 'Europe'),
('Antillas Holandesas', 'AN', 'North America'),
('Nueva Caledonia', 'NC', 'Oceania'),
('Nueva Zelanda', 'NZ', 'Oceania'),
('Nicaragua', 'NI', 'North America'),
('Níger', 'NE', 'Africa'),
('Nigeria', 'NG', 'Africa'),
('Niue', 'NU', 'Oceania'),
('Isla Norfolk', 'NF', 'Oceania'),
('Islas Marianas del Norte', 'MP', 'Oceania'),
('Noruega', 'NO', 'Europe'),
('Omán', 'OM', 'Asia'),
('Pakistán', 'PK', 'Asia'),
('Palau', 'PW', 'Oceania'),
('Territorio Palestino, Ocupado', 'PS', 'Asia'),
('Panamá', 'PA', 'North America'),
('Papúa Nueva Guinea', 'PG', 'Oceania'),
('Paraguay', 'PY', 'South America'),
('Perú', 'PE', 'South America'),
('Filipinas', 'PH', 'Asia'),
('Pitcairn', 'PN', 'Oceania'),
('Polonia', 'PL', 'Europe'),
('Portugal', 'PT', 'Europe'),
('Puerto Rico', 'PR', 'North America'),
('Qatar', 'QA', 'Asia'),
('Reunión', 'RE', 'Africa'),
('Rumania', 'RO', 'Europe'),
('Federación Rusa', 'RU', 'Asia'),
('Ruanda', 'RW', 'Africa'),
('San Bartolomé', 'BL', 'North America'),
('Santa Elena', 'SH', 'Africa'),
('Saint Kitts y Nevis', 'KN', 'North America'),
('Santa Lucía', 'LC', 'North America'),
('San Martín', 'MF', 'North America'),
('San Pedro y Miquelón', 'PM', 'North America'),
('San Vicente y las Granadinas', 'VC', 'North America'),
('Samoa', 'WS', 'Oceania'),
('San Marino', 'SM', 'Europe'),
('Santo Tomé y Príncipe', 'ST', 'Africa'),
('Arabia Saudita', 'SA', 'Asia'),
('Senegal', 'SN', 'Africa'),
('Serbia', 'RS', 'Europe'),
('Serbia y Montenegro', 'CS', 'Europe'),
('Seychelles', 'SC', 'Africa'),
('Sierra Leona', 'SL', 'Africa'),
('Singapur', 'SG', 'Asia'),
('Eslovaquia', 'SK', 'Europe'),
('Eslovenia', 'SI', 'Europe'),
('Islas Salomón', 'SB', 'Oceania'),
('Somalia', 'SO', 'Africa'),
('Sudáfrica', 'ZA', 'Africa'),
('Georgia del Sur y las Islas Sandwich del Sur', 'GS', 'Antarctica'),
('Sudán del Sur', 'SS', 'Africa'),
('España', 'ES', 'Europe'),
('Sri Lanka', 'LK', 'Asia'),
('Sudán', 'SD', 'Africa'),
('Surinam', 'SR', 'South America'),
('Svalbard y Jan Mayen', 'SJ', 'Europe'),
('Eswatini', 'SZ', 'Africa'),
('Suecia', 'SE', 'Europe'),
('Suiza', 'CH', 'Europe'),
('República Árabe Siria', 'SY', 'Asia'),
('Taiwán', 'TW', 'Asia'),
('Tayikistán', 'TJ', 'Asia'),
('Tanzania, República Unida de', 'TZ', 'Africa'),
('Tailandia', 'TH', 'Asia'),
('Timor-Leste', 'TL', 'Asia'),
('Togo', 'TG', 'Africa'),
('Tokelau', 'TK', 'Oceania'),
('Tonga', 'TO', 'Oceania'),
('Trinidad y Tobago', 'TT', 'North America'),
('Túnez', 'TN', 'Africa'),
('Turquía', 'TR', 'Asia'),
('Turkmenistán', 'TM', 'Asia'),
('Islas Turcas y Caicos', 'TC', 'North America'),
('Tuvalu', 'TV', 'Oceania'),
('Uganda', 'UG', 'Africa'),
('Ucrania', 'UA', 'Europe'),
('Emiratos Árabes Unidos', 'AE', 'Asia'),
('Reino Unido', 'GB', 'Europe'),
('Estados Unidos', 'US', 'North America'),
('Islas menores alejadas de los Estados Unidos', 'UM', 'North America'),
('Uruguay', 'UY', 'South America'),
('Uzbekistán', 'UZ', 'Asia'),
('Vanuatu', 'VU', 'Oceania'),
('Venezuela', 'VE', 'South America'),
('Vietnam', 'VN', 'Asia'),
('Islas Vírgenes Británicas', 'VG', 'North America'),
('Islas Vírgenes, EE. UU.', 'VI', 'North America'),
('Wallis y Futuna', 'WF', 'Oceania'),
('Sahara Occidental', 'EH', 'Africa'),
('Yemen', 'YE', 'Asia'),
('Zambia', 'ZM', 'Africa'),
('Zimbabue', 'ZW', 'Africa');


-- Insertamos Continentes a la tabla
INSERT INTO Continente (nombre)
SELECT DISTINCT continente FROM Pais;

-- Cambiamos los continentes a claves ajenas de continente
PRAGMA foreign_keys=off;

BEGIN TRANSACTION;

ALTER TABLE Pais RENAME TO Pais_old;

-- Crear la nueva tabla con la clave foránea para Continente
CREATE TABLE Pais (
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(255),
    iso VARCHAR(255),
    continente_id INTEGER,
    FOREIGN KEY (continente_id) REFERENCES Continente(id)
);

-- Insertar datos en la nueva tabla Pais
INSERT INTO Pais (nombre, iso, continente_id)
SELECT p.nombre, p.iso, c.id
FROM Pais_old p
JOIN Continente c ON p.continente = c.nombre;

-- Eliminar la tabla antigua
DROP TABLE Pais_old;

-- Confirmar la transacción
COMMIT;

-- Volver a activar las claves foráneas
PRAGMA foreign_keys=on;

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
  fotoPerfil TEXT,
  FOREIGN KEY (paisOrigen) REFERENCES Pais(id)
);

CREATE TABLE IF NOT EXISTS  Destino (
  id INTEGER PRIMARY KEY,
  titulo TEXT,
  descripcion TEXT,
  paisId INTEGER,
  numPuntuaciones INTEGER,
  sumaPuntuaciones INTEGER,
  numVisitas INTEGER DEFAULT 0,
  gastoTotal INTEGER,
  diasEstanciaTotal INTEGER,
  indiceSeguridad INTEGER,
  moneda TEXT,
  clima TEXT,
  FOREIGN KEY (paisId) REFERENCES Pais(id)
);

CREATE TABLE IF NOT EXISTS  Recomendacion (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  actividadId INTEGER,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (actividadId) REFERENCES Actividad(id),
  UNIQUE(usuarioId, actividadId)
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

CREATE TABLE IF NOT EXISTS  imgDestino (
  id INTEGER PRIMARY KEY,
  destinoId INTEGER,
  nombre TEXT UNIQUE,
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);

CREATE TABLE IF NOT EXISTS  imgActividad (
  id INTEGER PRIMARY KEY,
  actividadId INTEGER,
  nombre TEXT UNIQUE,
  FOREIGN KEY (actividadId) REFERENCES Actividad(id)
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
  FOREIGN KEY (destinoId) REFERENCES Destino(id),
  UNIQUE(usuarioId, destinoId)
);

CREATE TABLE IF NOT EXISTS  Visitados (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  destinoId INTEGER,
  fechaVisita INTEGER,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
  UNIQUE(usuarioId, destinoId)
);

CREATE TABLE IF NOT EXISTS  Historial (
  id INTEGER PRIMARY KEY,
  usuarioId INTEGER,
  destinoId INTEGER,
  fechaEntrado INTEGER,
  FOREIGN KEY (usuarioId) REFERENCES Usuario(id),
  FOREIGN KEY (destinoId) REFERENCES Destino(id)
);


