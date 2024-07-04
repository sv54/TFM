const bcrypt = require('bcrypt')

var usuariosEjemplo = [
	{
		nombre: 'Juan',
		email: 'juan@example.com',
		password: '123456',
		salt: 'salty123',
		paisOrigen: 211,
		metaViajes: 1,
		fotoPerfil: 'ruta/a/la/foto_juan.jpg'
	},
	{
		nombre: 'María',
		email: 'maria@example.com',
		password: '123456',
		salt: 'salty456',
		paisOrigen: 211,
		metaViajes: 2,
		fotoPerfil: 'ruta/a/la/foto_maria.jpg'
	},
	{
		nombre: 'Pedro',
		email: 'pedro@example.com',
		password: '123456',
		salt: 'salty789',
		paisOrigen: 211,
		metaViajes: 3,
		fotoPerfil: 'ruta/a/la/foto_pedro.jpg'
	}
];

const destinoEjemplo = [
	{
		titulo: "Madrid",
		descripcion: "Capital de España, conocida por su arte y cultura.",
		paisId: 211,
		numPuntuaciones: 500,
		sumaPuntuaciones: 2250,
		gastoTotal: 1000,
		diasEstanciaTotal: 5,
		indiceSeguridad: 8,
		moneda: "Euro",
		clima: "Mediterráneo"
	},
	{
		titulo: "Barcelona",
		descripcion: "Ciudad cosmopolita en la costa mediterránea.",
		paisId: 211,
		numPuntuaciones: 700,
		sumaPuntuaciones: 3000,
		gastoTotal: 1200,
		diasEstanciaTotal: 4,
		indiceSeguridad: 7,
		moneda: "Euro",
		clima: "Mediterráneo"
	},
	{
		titulo: "Alicante",
		descripcion: "Ciudad costera con hermosas playas y clima agradable.",
		paisId: 211,
		numPuntuaciones: 300,
		sumaPuntuaciones: 1400,
		gastoTotal: 800,
		diasEstanciaTotal: 7,
		indiceSeguridad: 9,
		moneda: "Euro",
		clima: "Mediterráneo"
	},
	{
		titulo: "París",
		descripcion: "Capital de Francia, conocida por su moda y gastronomía.",
		paisId: 76,
		numPuntuaciones: 900,
		sumaPuntuaciones: 4250,
		gastoTotal: 1500,
		diasEstanciaTotal: 6,
		indiceSeguridad: 8,
		moneda: "Euro",
		clima: "Templado"
	},
	{
		titulo: "Roma",
		descripcion: "La Ciudad Eterna, llena de historia y ruinas antiguas.",
		paisId: 110,
		numPuntuaciones: 800,
		sumaPuntuaciones: 3750,
		gastoTotal: 1300,
		diasEstanciaTotal: 5,
		indiceSeguridad: 7,
		moneda: "Euro",
		clima: "Mediterráneo"
	},
	{
		titulo: "Berlín",
		descripcion: "Capital de Alemania, con una vibrante vida nocturna y rica historia.",
		paisId: 83,
		numPuntuaciones: 600,
		sumaPuntuaciones: 2750,
		gastoTotal: 1100,
		diasEstanciaTotal: 4,
		indiceSeguridad: 8,
		moneda: "Euro",
		clima: "Templado"
	},
	{
		titulo: "Kiev",
		descripcion: "Capital de Ucrania, con una mezcla de arquitectura histórica y moderna.",
		paisId: 235,
		numPuntuaciones: 400,
		sumaPuntuaciones: 1750,
		gastoTotal: 900,
		diasEstanciaTotal: 6,
		indiceSeguridad: 6,
		moneda: "Grivna",
		clima: "Continental"
	},
	{
		titulo: "Londres",
		descripcion: "Capital del Reino Unido, famosa por su cultura y arquitectura.",
		paisId: 237,
		numPuntuaciones: 1000,
		sumaPuntuaciones: 4500,
		gastoTotal: 1700,
		diasEstanciaTotal: 7,
		indiceSeguridad: 9,
		moneda: "Libra esterlina",
		clima: "Templado"
	},
	{
		titulo: "Tokio",
		descripcion: "Capital de Japón, una metrópolis moderna con una rica historia y cultura.",
		paisId: 112,
		numPuntuaciones: 1200,
		sumaPuntuaciones: 5500,
		gastoTotal: 2000,
		diasEstanciaTotal: 8,
		indiceSeguridad: 8,
		moneda: "Yen",
		clima: "Templado"
	},
	{
		titulo: "Sidney",
		descripcion: "Ciudad costera en Australia, conocida por sus playas y estilo de vida relajado.",
		paisId: 14,
		numPuntuaciones: 900,
		sumaPuntuaciones: 4000,
		gastoTotal: 1500,
		diasEstanciaTotal: 7,
		indiceSeguridad: 9,
		moneda: "Dólar australiano",
		clima: "Subtropical"
	},
	{
		titulo: "Nueva York",
		descripcion: "La ciudad que nunca duerme, llena de diversidad cultural y actividades.",
		paisId: 238,
		numPuntuaciones: 1500,
		sumaPuntuaciones: 7000,
		gastoTotal: 2500,
		diasEstanciaTotal: 10,
		indiceSeguridad: 7,
		moneda: "Dólar estadounidense",
		clima: "Templado"
	},
	{
		titulo: "Dubai",
		descripcion: "Ciudad en los Emiratos Árabes Unidos, conocida por su lujo y arquitectura futurista.",
		paisId: 197,
		numPuntuaciones: 800,
		sumaPuntuaciones: 3750,
		gastoTotal: 1800,
		diasEstanciaTotal: 6,
		indiceSeguridad: 8,
		moneda: "Dirham",
		clima: "Desértico"
	},
	{
		titulo: "Toronto",
		descripcion: "Ciudad multicultural en Canadá, famosa por su diversidad y paisajes urbanos.",
		paisId: 40,
		numPuntuaciones: 700,
		sumaPuntuaciones: 3250,
		gastoTotal: 1400,
		diasEstanciaTotal: 5,
		indiceSeguridad: 8,
		moneda: "Dólar canadiense",
		clima: "Templado"
	},
	{
		titulo: "Seúl",
		descripcion: "Capital de Corea del Sur, una ciudad moderna con una mezcla de tradición y tecnología.",
		paisId: 119,
		numPuntuaciones: 600,
		sumaPuntuaciones: 2750,
		gastoTotal: 1200,
		diasEstanciaTotal: 6,
		indiceSeguridad: 7,
		moneda: "Won",
		clima: "Continental"
	},
	{
		titulo: "Estambul",
		descripcion: "Ciudad transcontinental entre Europa y Asia, con una rica historia y cultura.",
		paisId: 230,
		numPuntuaciones: 500,
		sumaPuntuaciones: 2250,
		gastoTotal: 1000,
		diasEstanciaTotal: 7,
		indiceSeguridad: 7,
		moneda: "Lira turca",
		clima: "Mediterráneo"
	},
	{
		titulo: "Bangkok",
		descripcion: "Capital de Tailandia, famosa por sus templos budistas y su animada vida nocturna.",
		paisId: 223,
		numPuntuaciones: 700,
		sumaPuntuaciones: 3750,
		gastoTotal: 1300,
		diasEstanciaTotal: 6,
		indiceSeguridad: 8,
		moneda: "Baht",
		clima: "Tropical"
	},
	{
		titulo: "Buenos Aires",
		descripcion: "Capital de Argentina, conocida por su arquitectura elegante y su vibrante cultura.",
		paisId: 11,
		numPuntuaciones: 800,
		sumaPuntuaciones: 3700,
		gastoTotal: 1400,
		diasEstanciaTotal: 7,
		indiceSeguridad: 8,
		moneda: "Peso argentino",
		clima: "Subtropical"
	}
];

const actividadesEjemplo = [
	// Actividades para Roma
	{
		titulo: "Coliseo",
		descripcion: "Descubre la historia del Coliseo Romano y su importancia en la antigua Roma.",
		numRecomendado: 0,
		destinoId: 1 // Suponiendo que el ID del destino de Roma es 1
	},
	{
		titulo: "Vaticano",
		descripcion: "Explora los museos del Vaticano y admira las obras maestras de artistas como Miguel Ángel y Rafael.",
		numRecomendado: 0,
		destinoId: 1
	},
	{
		titulo: "Foro Romano",
		descripcion: "Recorre las ruinas del Foro Romano y conoce más sobre la vida en la antigua Roma.",
		numRecomendado: 0,
		destinoId: 1
	},
	// Actividades para Kiev
	{
		titulo: "Monasterio de las Cuevas de Kiev",
		descripcion: "Explora este complejo monástico ortodoxo, un sitio declarado Patrimonio de la Humanidad por la UNESCO.",
		numRecomendado: 0,
		destinoId: 2 // Suponiendo que el ID del destino de Kiev es 2
	},
	{
		titulo: "Plaza de la Independencia",
		descripcion: "Disfruta de la arquitectura y la vida diaria en la plaza más grande de Kiev.",
		numRecomendado: 0,
		destinoId: 2
	},
	{
		titulo: "Museo Nacional de Historia de Ucrania",
		descripcion: "Descubre la historia y la cultura de Ucrania a través de las colecciones del museo.",
		numRecomendado: 0,
		destinoId: 2
	},
	// Actividades para Madrid
	{
		titulo: "Museo del Prado",
		descripcion: "Contempla obras maestras de artistas como Velázquez, Goya y El Greco en uno de los museos más importantes del mundo.",
		numRecomendado: 0,
		destinoId: 3 // Suponiendo que el ID del destino de Madrid es 3
	},
	{
		titulo: "Parque del Retiro",
		descripcion: "Relájate en este hermoso parque y disfruta de sus jardines, estanques y actividades al aire libre.",
		numRecomendado: 0,
		destinoId: 3
	},
	{
		titulo: "Palacio Real",
		descripcion: "Descubre la residencia oficial de la monarquía española y admira su impresionante arquitectura y decoración.",
		numRecomendado: 0,
		destinoId: 3
	},
	// Actividades para Alicante
	{
		titulo: "Castillo de Santa Bárbara",
		descripcion: "Contempla las impresionantes vistas de la ciudad y el mar Mediterráneo desde este castillo medieval.",
		numRecomendado: 0,
		destinoId: 4 // Suponiendo que el ID del destino de Alicante es 4
	},
	{
		titulo: "Casco Antiguo",
		descripcion: "Recorre las estrechas calles del casco antiguo y descubre la historia y la cultura de Alicante.",
		numRecomendado: 0,
		destinoId: 4
	},
	{
		titulo: "Playa del Postiguet",
		descripcion: "Disfruta del sol y el mar en esta popular playa urbana situada en el corazón de Alicante.",
		numRecomendado: 0,
		destinoId: 4
	},
	// Actividades para París
	{
		titulo: "Torre Eiffel",
		descripcion: "Contempla las impresionantes vistas de París desde la icónica Torre Eiffel.",
		numRecomendado: 0,
		destinoId: 5 // Suponiendo que el ID del destino de París es 5
	},
	{
		titulo: "Museo del Louvre",
		descripcion: "Explora una de las colecciones de arte más grandes del mundo en este famoso museo.",
		numRecomendado: 0,
		destinoId: 5
	},
	{
		titulo: "Montmartre",
		descripcion: "Descubre el encanto bohemio de este barrio histórico y visita la Basílica del Sagrado Corazón.",
		numRecomendado: 0,
		destinoId: 5
	},
	// Actividades para Londres
	{
		titulo: "Palacio de Buckingham",
		descripcion: "Observa el famoso cambio de guardia y admira la residencia oficial de la monarquía británica.",
		numRecomendado: 0,
		destinoId: 6 // Suponiendo que el ID del destino de Londres es 6
	},
	{
		titulo: "Museo Británico",
		descripcion: "Explora las colecciones del museo que abarcan miles de años de historia y cultura.",
		numRecomendado: 0,
		destinoId: 6
	},
	{
		titulo: "London Eye",
		descripcion: "Disfruta de vistas panorámicas de Londres desde esta famosa noria.",
		numRecomendado: 0,
		destinoId: 6
	},
	// Actividades para Tokio
	{
		titulo: "Templo Senso-ji",
		descripcion: "Explora el templo más antiguo de Tokio y admira su impresionante arquitectura y ambiente histórico.",
		numRecomendado: 0,
		destinoId: 7 // Suponiendo que el ID del destino de Tokio es 7
	},
	{
		titulo: "Shibuya",
		descripcion: "Sumérgete en la vibrante vida urbana de Tokio y experimenta la famosa intersección de Shibuya.",
		numRecomendado: 0,
		destinoId: 7
	},
	{
		titulo: "Palacio Imperial",
		descripcion: "Descubre la residencia oficial del Emperador de Japón y explora sus hermosos jardines.",
		numRecomendado: 0,
		destinoId: 7
	},
	// Actividades para Estambul
	{
		titulo: "Mezquita Azul",
		descripcion: "Admira la impresionante arquitectura y los hermosos azulejos de esta famosa mezquita.",
		numRecomendado: 0,
		destinoId: 8 // Suponiendo que el ID del destino de Estambul es 8
	},
	{
		titulo: "Gran Bazar",
		descripcion: "Explora uno de los mercados cubiertos más grandes del mundo y descubre una gran variedad de productos.",
		numRecomendado: 0,
		destinoId: 8
	},
	{
		titulo: "Palacio de Topkapi",
		descripcion: "Descubre la historia otomana y explora los exquisitos jardines y salones del palacio.",
		numRecomendado: 0,
		destinoId: 8
	},
	// Actividades para Berlín
	{
		titulo: "Muro de Berlín",
		descripcion: "Recorre el famoso Muro de Berlín y aprende sobre su importancia histórica en la ciudad.",
		numRecomendado: 0,
		destinoId: 9 // Suponiendo que el ID del destino de Berlín es 9
	},
	{
		titulo: "Museo de Pérgamo",
		descripcion: "Explora las impresionantes colecciones de arte y arqueología del museo.",
		numRecomendado: 0,
		destinoId: 9
	},
	{
		titulo: "Parque Tiergarten",
		descripcion: "Relájate en el parque más grande de Berlín y disfruta de la naturaleza y la tranquilidad.",
		numRecomendado: 0,
		destinoId: 9
	}
];

const comentariosEjemplo = [
	{
		"usuarioId": 1,
		"destinoId": "",
		"texto": "¡Qué lugar tan maravilloso! Disfruté mucho de mi visita.",
		"permisoExtraInfo": true,
		"estanciaDias": 5,
		"dineroGastado": 200,
		"valoracion": 5
	},
	{
		"usuarioId": 2,
		"destinoId": "",
		"texto": "¡Increíble experiencia! Recomiendo este lugar a todos.",
		"permisoExtraInfo": false,
		"estanciaDias": 3,
		"dineroGastado": 150,
		"valoracion": 4
	},
	{
		"usuarioId": 3,
		"destinoId": "",
		"texto": "Me encantó cada momento que pasé aquí. Volveré pronto.",
		"permisoExtraInfo": true,
		"estanciaDias": 7,
		"dineroGastado": 300,
		"valoracion": 5
	},
	{
		"usuarioId": 4,
		"destinoId": "",
		"texto": "El lugar es hermoso, pero la comida no me gustó tanto.",
		"permisoExtraInfo": false,
		"estanciaDias": 4,
		"dineroGastado": 180,
		"valoracion": 3
	},
	{
		"usuarioId": 1,
		"destinoId": "",
		"texto": "Experiencia única. Definitivamente regresaré en el futuro.",
		"permisoExtraInfo": true,
		"estanciaDias": 6,
		"dineroGastado": 250,
		"valoracion": 5
	},
	{
		"usuarioId": 2,
		"destinoId": "",
		"texto": "No puedo esperar para volver a este lugar. Me encantó todo.",
		"permisoExtraInfo": true,
		"estanciaDias": 4,
		"dineroGastado": 220,
		"valoracion": 5
	},
	{
		"usuarioId": 3,
		"destinoId": "",
		"texto": "Me sorprendió gratamente este destino. Lo recomiendo totalmente.",
		"permisoExtraInfo": true,
		"estanciaDias": 5,
		"dineroGastado": 190,
		"valoracion": 4
	},
	{
		"usuarioId": 4,
		"destinoId": "",
		"texto": "Buena experiencia en general. Me gustaría volver en el futuro.",
		"permisoExtraInfo": false,
		"estanciaDias": 3,
		"dineroGastado": 160,
		"valoracion": 4
	},
	{
		"usuarioId": 1,
		"destinoId": "",
		"texto": "El destino superó mis expectativas. Fue un viaje increíble.",
		"permisoExtraInfo": true,
		"estanciaDias": 7,
		"dineroGastado": 280,
		"valoracion": 5
	},
	{
		"usuarioId": 2,
		"destinoId": "",
		"texto": "¡Volveré sin duda alguna! Este lugar tiene tanto que ofrecer.",
		"permisoExtraInfo": true,
		"estanciaDias": 4,
		"dineroGastado": 230,
		"valoracion": 5
	},
	{
		"usuarioId": 3,
		"destinoId": "",
		"texto": "Me enamoré de este destino desde el primer día. Fue mágico.",
		"permisoExtraInfo": true,
		"estanciaDias": 6,
		"dineroGastado": 270,
		"valoracion": 5
	}
];

const nombresImgActividades = ['Buckingham1.jpg', 'Buckingham2.png', 'Buckingham3.jpg', 'CascoAntiguo1.jpg', 'CascoAntiguo2.jpg', 'CascoAntiguo3.jpg', 'CastilloDeSantaBarbara1.jpg', 'CastilloDeSantaBarbara2.jpg', 'CastilloDeSantaBarbara3.jpg', 'Coliseo1.jpg', 'Coliseo2.jpg', 'Coliseo3.jpeg', 'ForoRomano1.jpg', 'ForoRomano2.png', 'ForoRomano3.jpg', 'GranBazar1.jpg', 'GranBazar2.jpg', 'LondonEye1.JPG', 'LondonEye2.png', 'LondonEye3.jpg', 'Louvre1.png', 'Louvre2.png', 'Louvre3.jpeg', 'MezquitaAzul1.jpg', 'MezquitaAzul2.jpeg', 'MonasterioCuevasKiev1.jpg', 'MonasterioCuevasKiev2.jpg', 'Montmartre1.jpg', 'Montmartre2.JPG', 'Montmartre3.jpg', 'MuroDeBerlin1.jpg', 'MuroDeBerlin2.png', 'MuseoBritanico1.jpg', 'MuseoBritanico2.jpg', 'MuseoDelPrado1.jpg', 'MuseodelPrado2.png', 'MuseoDelPrado3.png', 'MuseoDePergamo1.jpg', 'MuseoDePergamo2.jpg', 'MuseoDePergamo3.jpeg', 'MuseoNacionalDeHistoriaDeUcrania1.jpg', 'MuseoNacionalDeHistoriaDeUcrania2.jpg', 'MuseoNacionalDeHistoriaDeUcrania3.jpg', 'PalacioImperial1.jpg', 'PalacioImperial2.jpg', 'PalacioImperial3.jpg', 'PalacioReal1.jpg', 'PalacioReal2.jpg', 'PalacioReal3.jpg', 'PalacioTopkapi1.jpg', 'PalacioTopkapi2.jpg', 'PalacioTopkapi3.jpg', 'ParqueDelRetiro1.jpg', 'ParqueDelRetiro2.png', 'ParqueDelRetiro3.png', 'PlayaDelPostiguet1.jpg', 'PlayaDelPostiguet2.jpg', 'PlazaDeLaIndependencia1.jpg', 'PlazaDeLaIndependencia2.jpg', 'Senso-ji1.jpg', 'Senso-ji2.jpg', 'Senso-ji3.jpg', 'Shibuya1.jpg', 'Shibuya2.png', 'Shibuya3.jpg', 'TorreEiffel1.jpg', 'TorreEiffel2.jpg', 'TorreEiffel3.png', 'Vaticano1.png', 'Vaticano2.jpg'];

const nombresImgDestinos = ['Alicante1.jpg', 'Alicante2.png', 'Bangkok1.jpg', 'Bangkok2.jpg', 'Bangkok3.jpg', 'Barcelona1.jpg', 'Barcelona2.jpg', 'Barcelona3.png', 'Berlín1.jpg', 'Berlín2.png', 'Berlín3.png', 'Berlín4.jpg', 'Buenos Aires1.png', 'Buenos Aires2.jpeg', 'Buenos Aires3.jpg', 'Dubai1.jpg', 'Dubai2.png', 'Estambul1.png', 'Estambul2.jpeg', 'Kiev1.jpg', 'Kiev2.jpg', 'Kiev3.jpg', 'Londres1.jpg', 'Londres2.jpg', 'Londres3.png', 'Madrid1.png', 'Madrid2.png', 'Madrid3.jpg', 'Nueva York1.jpg', 'Nueva York2.jpg', 'París1.jpg', 'París2.png', 'París3.jpg', 'Roma1.jpg', 'Roma2.jpg', 'Roma3.jpg', 'Seúl1.jpg', 'Seúl2.jpg', 'Seúl3.jpg', 'Sidney1.png', 'Sidney2.jpg', 'Sidney3.png', 'Tokio1.jpg', 'Tokio2.jpg', 'Tokio3.jpg', 'Toronto1.png', 'Toronto2.jpg', 'Toronto3.png'];




const usuarioSerhii = {
	nombre: 'Serhii',
	email: 'serhii@example.com',
	password: '123456',
	salt: null,
	paisOrigen: 211,
	metaViajes: 3,
	fotoPerfil: 'serhii.jpg'
}

const sqlite3 = require('sqlite3').verbose();

const dbPath = 'TFMDB.db';
var db = new sqlite3.Database(dbPath);



async function getSalt() {
	const saltSerhii = bcrypt.genSalt(10);
	const salt = await saltSerhii;
	return salt;
}

async function getPass(contrasenya, salt) {

	pass = bcrypt.hash(contrasenya, salt)
	return pass;
}

async function insertarSerhii() {
	const salt = await getSalt()
	const pass = await getPass(usuarioSerhii.password, salt)

	usuarioSerhii.salt = salt
	usuarioSerhii.password = pass

	insertUsuario(usuarioSerhii, ConsoleLog)
}

function getAllDestinos(callback) {
	const sqlQuery = 'SELECT * FROM Destino';

	db.all(sqlQuery, [], (err, rows) => {
		if (err) {
			console.error('Error al obtener destinos:', err.message);
			callback(err, null);
			return;
		}
		// Devolver los destinos obtenidos
		callback(null, rows);
	});
}

function getAllUsuarios(callback) {
	const sqlQuery = 'SELECT * FROM Usuario';

	db.all(sqlQuery, [], (err, rows) => {
		if (err) {
			console.error('Error al obtener usuarios:', err.message);
			callback(err, null);
			return;
		}
		// Devolver los destinos obtenidos
		callback(null, rows);
	});
}



// insertarSerhii()
// CheckIfBDNull()
// getAllDestinos(ConsoleLog)
// getAllUsuarios(ConsoleLog)

async function poblarUsuarios() {
	for (let i = 0; i < usuariosEjemplo.length; i++) {
		await insertUsuario(usuariosEjemplo[i], ConsoleLog)
		// console.log(usuariosEjemplo[i].nombre);
	}
}

async function poblarDestino() {
	for (let i = 0; i < destinoEjemplo.length; i++) {
		await insertDestino(destinoEjemplo[i], ConsoleLog)
		//console.log(destinoEjemplo[i].titulo);
	}
}

function ConsoleLog(mensaje, rows) {
	if (rows != null) {
		// console.log('Callback:', rows);
	}
	else if (mensaje != null) {
		console.log('Callback:', mensaje);
	}
	else {
		// console.log('Callback:', "NULL");
	}
}

async function CheckIfBDNull() {
	const sqlQuery = 'SELECT COUNT(*) AS count FROM Destino';

	db.get(sqlQuery, async (err, row) => {
		if (err) {
			console.error('Error al buscar destino:', err.message);
			return;
		}
		if (row.count == 0) {
			console.log("La base de datos esta vacia! Se rellena con datos ejemplo...")
			await poblarDestino()
			await sleep(1000)
			await poblarActividad()
			await sleep(1000)
			await poblarUsuarios()
			await sleep(1000)
			await poblarImgDestino()
			await poblarImgActividad(ConsoleLog)
			await poblarComentarios();
			await poblarVisitados();
			await poblarHistoria();
			await poblarFavoritos();

			insertarSerhii()


		}
	});
}

async function insertUsuario(usuarioData, callback) {
	const sqlQuery = `INSERT INTO Usuario (nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)`;
	const { nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil } = usuarioData;
	db.run(sqlQuery, [nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil], function (err) {
		if (err) {
			console.error('Error al insertar usuario:', err.message);
			callback(err, null);
			return;
		}
		// Obtener el ID del usuario insertado
		callback(null, this.lastID);
	});
}

async function insertDestino(destinoData, callback) {
	const sqlQuery = `INSERT INTO Destino (titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima, numVisitas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, `+ generarNumeroAleatorio(1,1150) +`)`;
	const { titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima } = destinoData;
	db.run(sqlQuery, [titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima], function (err) {
		if (err) {
			console.error('Error al insertar destino:', err.message);
			callback(err, null);
			return;
		}
		// Obtener el ID del destino insertado
		callback(null, this.lastID);
	});
}
function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}
async function insertActividad(actividadData, destinoName, callback) {
	// Primero, consulta el ID del destino utilizando el nombre proporcionado
	const sqlSelectDestinoId = `SELECT id FROM Destino WHERE titulo = ?`;
	db.get(sqlSelectDestinoId, [destinoName], (err, row) => {
		if (err) {
			console.error('Error al obtener el ID del destino:', err.message);
			callback(err, null);
			return;
		}
		if (!row) {
			// Si no se encuentra ningún destino con el nombre proporcionado
			const error = new Error('No se encontró ningún destino con el nombre proporcionado: ' + destinoName + ";");
			callback(error, null);
			return;
		}
		const destinoId = row.id;

		// Insertar la actividad utilizando el ID del destino obtenido
		const sqlQuery = `INSERT INTO Actividad (titulo, descripcion, numRecomendado, destinoId) VALUES (?, ?, ?, ?)`;
		const { titulo, descripcion, numRecomendado } = actividadData;
		db.run(sqlQuery, [titulo, descripcion, numRecomendado, destinoId], function (err) {
			if (err) {
				console.error('Error al insertar actividad:', err.message);
				callback(err, null);
				return;
			}
			// Obtener el ID de la actividad insertada
			callback(null, this.lastID);
		});
	});
}
async function poblarActividad() {
	for (var i = 0; i < actividadesEjemplo.length; i++) {
		var titulo = ""
		if (i >= 0 && i <= 2) {
			titulo = "Roma"
		}
		else if (i >= 3 && i <= 5) {
			titulo = "Kiev"
		}
		else if (i >= 6 && i <= 8) {
			titulo = "Madrid"
		}
		else if (i >= 9 && i <= 11) {
			titulo = "Alicante"
		}
		else if (i >= 12 && i <= 14) {
			titulo = "París"
		}
		else if (i >= 15 && i <= 17) {
			titulo = "Londres"
		}
		else if (i >= 18 && i <= 20) {
			titulo = "Tokio"
		}
		else if (i >= 21 && i <= 22) {
			titulo = "Estambul"
		}
		else if (i >= 23 && i <= 25) {
			titulo = "Berlín"
		}
		await insertActividad(actividadesEjemplo[i], titulo, ConsoleLog)
	}
}

async function RevisarNombresDestinos() {
	var nombreDestino = '';
	var contador = 0;

	for (var i = 0; i < nombresImgDestinos.length; i++) {
		nombreDestino = quitarExtension(nombresImgDestinos[i]);

		try {
			const row = await obtenerIdDestinoAsync(nombreDestino);
			if (row) {
				contador++;
			}
		} catch (err) {
			console.error('Error al obtener el ID del destino:', err.message);
		}
	}

	console.log("correctos: " + contador);
}

async function obtenerIdDestinoAsync(nombreDestino) {
	return new Promise((resolve, reject) => {
		const sqlSelectDestinoId = `SELECT id FROM Destino WHERE titulo = ?`;
		db.get(sqlSelectDestinoId, [nombreDestino], (err, row) => {
			if (err) {
				reject(err);
			} else {
				resolve(row);
			}
		});
	});
}

function quitarExtension(nombreArchivo) {
	const partes = nombreArchivo.split('.');
	partes.pop();
	var nombreSinExtension = partes.join('');
	nombre = nombreSinExtension.replace(/\d/g, '');
	return nombre;
}

async function insertarImgDestino(destinoName, nombreFichero, callback) {
	const sqlSelectDestinoId = `SELECT id FROM Destino WHERE titulo = ?`;
	db.get(sqlSelectDestinoId, [destinoName], (err, row) => {
		if (err) {
			console.error('Error al obtener el ID del destino:', err.message);
			callback(err, null);
			return;
		}
		if (!row) {
			// Si no se encuentra ningún destino con el nombre proporcionado
			const error = new Error('No se encontró ningún destino con el nombre proporcionado: ' + destinoName + ";");
			callback(error, null);
			return;
		}
		const destinoId = row.id;

		// Insertar la actividad utilizando el ID del destino obtenido
		const sqlQuery = `INSERT INTO imgDestino (destinoId, nombre) VALUES (?, ?)`;

		db.run(sqlQuery, [destinoId, nombreFichero], function (err) {
			if (err) {
				console.error('Error al insertar actividad:', err.message);
				callback(err, null);
				return;
			}
			// Obtener el ID de la actividad insertada
			callback(null, this.lastID);
		});
	});
}
async function poblarImgDestino() {
	var nombreDestino = ""
	for (var i = 0; i < nombresImgDestinos.length; i++) {
		nombreDestino = quitarExtension(nombresImgDestinos[i])
		await insertarImgDestino(nombreDestino, nombresImgDestinos[i], ConsoleLog)
	}
}

async function poblarImgActividad(callback) {
	const sqlAct = `SELECT id, titulo FROM Actividad`;
	db.all(sqlAct, async (err, rows) => {
		if (err) {
			console.error('Error al obtener actividades', err.message);
			callback(err, null);
			return;
		}
		if (!rows) {
			const error = new Error('No se encontró ningúna activdad');
			callback(error, null);
			return;
		}
		else {
			var img1 = "", img2 = "", img3 = "";
			for (const row of rows) {
				img1 = "";
				img2 = "";
				img3 = "";
				if (row.titulo == "Coliseo") {
					img1 = "Coliseo1.jpg";
					img2 = "Coliseo2.jpg";
					img3 = "Coliseo3.jpeg";
				} else if (row.titulo == "Vaticano") {
					img1 = "Vaticano1.png";
					img2 = "Vaticano2.jpg";
					img3 = "";
				} else if (row.titulo == "Museo del Prado") {
					img1 = "MuseoDelPrado1.jpg";
					img2 = "MuseodelPrado2.png";
					img3 = "MuseoDelPrado3.png";
				} else if (row.titulo == "Parque del Retiro") {
					img1 = "ParqueDelRetiro1.jpg";
					img2 = "ParqueDelRetiro2.png";
					img3 = "ParqueDelRetiro3.png";
				} else if (row.titulo == "Palacio Real") {
					img1 = "PalacioReal1.jpg";
					img2 = "PalacioReal2.jpg";
					img3 = "PalacioReal3.jpg";
				} else if (row.titulo == "Castillo de Santa Bárbara") {
					img1 = "CastilloDeSantaBarbara1.jpg";
					img2 = "CastilloDeSantaBarbara2.jpg";
					img3 = "CastilloDeSantaBarbara3.jpg";
				} else if (row.titulo == "Plaza de la Independencia") {
					img1 = "PlazaDeLaIndependencia1.jpg";
					img2 = "PlazaDeLaIndependencia2.jpg";
					img3 = "";
				} else if (row.titulo == "Torre Eiffel") {
					img1 = "TorreEiffel1.jpg";
					img2 = "TorreEiffel2.jpg";
					img3 = "TorreEiffel3.png";
				} else if (row.titulo == "Museo del Louvre") {
					img1 = "Louvre1.png";
					img2 = "Louvre2.png";
					img3 = "Louvre3.jpeg";
				} else if (row.titulo == "Montmartre") {
					img1 = "Montmartre1.jpg";
					img2 = "Montmartre2.JPG";
					img3 = "Montmartre3.jpg";
				} else if (row.titulo == "Palacio de Buckingham") {
					img1 = "Buckingham1.jpg";
					img2 = "Buckingham2.png";
					img3 = "Buckingham3.jpg";
				} else if (row.titulo == "Museo Británico") {
					img1 = "MuseoBritanico1.jpg";
					img2 = "MuseoBritanico2.jpg";
					img3 = "";
				} else if (row.titulo == "London Eye") {
					img1 = "LondonEye1.JPG";
					img2 = "LondonEye2.png";
					img3 = "LondonEye3.jpg";
				} else if (row.titulo == "Casco Antiguo") {
					img1 = "CascoAntiguo1.jpg";
					img2 = "CascoAntiguo2.jpg";
					img3 = "CascoAntiguo3.jpg";
				} else if (row.titulo == "Shibuya") {
					img1 = "Shibuya1.jpg";
					img2 = "Shibuya2.png";
					img3 = "Shibuya3.jpg";
				} else if (row.titulo == "Palacio Imperial") {
					img1 = "PalacioImperial1.jpg";
					img2 = "PalacioImperial2.jpg";
					img3 = "PalacioImperial3.jpg";
				} else if (row.titulo == "Mezquita Azul") {
					img1 = "MezquitaAzul1.jpg";
					img2 = "MezquitaAzul2.jpeg";
					img3 = "";
				} else if (row.titulo == "Gran Bazar") {
					img1 = "GranBazar1.jpg";
					img2 = "GranBazar2.jpg";
					img3 = "";
				} else if (row.titulo == "Monasterio de las Cuevas de Kiev") {
					img1 = "MonasterioCuevasKiev1.jpg";
					img2 = "MonasterioCuevasKiev2.jpg";
					img3 = "";
				} else if (row.titulo == "Foro Romano") {
					img1 = "ForoRomano1.jpg";
					img2 = "ForoRomano2.png";
					img3 = "ForoRomano3.jpg";
				} else if (row.titulo == "Museo Nacional de Historia de Ucrania") {
					img1 = "MuseoNacionalDeHistoriaDeUcrania1.jpg";
					img2 = "MuseoNacionalDeHistoriaDeUcrania2.jpg";
					img3 = "MuseoNacionalDeHistoriaDeUcrania3.jpg";
				} else if (row.titulo == "Templo Senso-ji") {
					img1 = "Senso-ji1.jpg";
					img2 = "Senso-ji2.jpg";
					img3 = "Senso-ji3.jpg";
				} else if (row.titulo == "Playa del Postiguet") {
					img1 = "PlayaDelPostiguet1.jpg";
					img2 = "PlayaDelPostiguet2.jpg";
					img3 = "";
				} else if (row.titulo == "Palacio de Topkapi") {
					img1 = "PalacioTopkapi1.jpg";
					img2 = "PalacioTopkapi2.jpg";
					img3 = "PalacioTopkapi3.jpg";
				} else if (row.titulo == "Muro de Berlín") {
					img1 = "MuroDeBerlin1.jpg";
					img2 = "MuroDeBerlin2.png";
					img3 = "";
				} else if (row.titulo == "Museo de Pérgamo") {
					img1 = "MuseoDePergamo1.jpg";
					img2 = "MuseoDePergamo2.jpg";
					img3 = "MuseoDePergamo3.jpeg";
				}

				await insertarImgActividad(row.id, img1, ConsoleLog);
				await insertarImgActividad(row.id, img2, ConsoleLog);
				if (img3 != "") {
					await insertarImgActividad(row.id, img3, ConsoleLog)
				}
			}
		}


	});
}

async function insertarImgActividad(actividadId, nombre, callback) {
	const sqlQuery = `INSERT INTO imgActividad (actividadId, nombre) VALUES (?, ?)`;
	db.run(sqlQuery, [actividadId, nombre], function (err) {
		if (err) {
			console.error('Error al insertar imagen de actividad:', err.message, "::::" + " " + nombre + " ::: " + actividadId);
			callback(err, null);
			return;
		}
		// Obtener el ID de la actividad insertada
		callback(null, this.lastID);

	});
}

async function insertComentario(destinoId, comentario) {
	const sqlInsertarComentario = `
	INSERT INTO Comentario (usuarioId, destinoId, texto, permisoExtraInfo, estanciaDias, dineroGastado, valoracion)
	VALUES (?, ?, ?, ?, ?, ?, ?)
	`;
	const params = [
		comentario.usuarioId,
		destinoId,
		comentario.texto,
		comentario.permisoExtraInfo,
		comentario.estanciaDias,
		comentario.dineroGastado,
		comentario.valoracion
	];

	db.run(sqlInsertarComentario, params, function (err) {
		if (err) {
			console.error('Error al insertar comentario:', err.message);
			return;
		}
		// console.log(`Comentario insertado con ID ${this.lastID}`);
	});
}

async function poblarComentarios() {
	const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
	db.all(sqlSeleccionarDestinos, async (err, rows) => {
		if (err) {
			console.error('Error al seleccionar los destinos:', err.message);
			return;
		}
		for (const row of rows) {
			const destinoId = row.id;
			const repeticiones = 2;
			for (var i = 0; i < 2; i++) {
				for (const comentario of comentariosEjemplo) {
					comentario.texto = comentario.texto + " Id: " + destinoId;
					await insertComentario(destinoId, comentario);
				}
			}
		}
	});
}


function generarNumeroAleatorio(min, max) {
	// Generar un número aleatorio entre 0 (inclusive) y 1 (exclusivo)
	const random = Math.random();
	// Escalar el número aleatorio al rango deseado y redondearlo
	const numeroAleatorio = Math.floor(random * (max - min + 1)) + min;
	return numeroAleatorio;
}

// poblarComentarios();

async function poblarVisitados() {
    const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
    db.all(sqlSeleccionarDestinos, async (err, rows) => {
        if (err) {
            console.error('Error al seleccionar los destinos:', err.message);
            return;
        }
        const usuarioIds = [1, 2, 3, 4];
        for (const row of rows) {
            const destinoId = row.id;
            const fechaMarcado = new Date().toISOString();
            const usuarioId = usuarioIds[Math.floor(Math.random() * usuarioIds.length)];
            await insertVisitado(usuarioId, destinoId, fechaMarcado);
        }
    });
}

async function insertVisitado(usuarioId, destinoId, fechaMarcado) {
    const sqlInsertarVisitado = `
        INSERT INTO Visitados (usuarioId, destinoId, fechaMarcado)
        VALUES (?, ?, ?)
    `;
    const params = [usuarioId, destinoId, fechaMarcado];

    db.run(sqlInsertarVisitado, params, function (err) {
        if (err) {
            console.error('Error al insertar visitado:', err.message);
            return;
        }
        // console.log(`Visitado insertado con ID ${this.lastID}`);
    });
}

async function poblarHistoria() {
    const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
    db.all(sqlSeleccionarDestinos, async (err, rows) => {
        if (err) {
            console.error('Error al seleccionar los destinos:', err.message);
            return;
        }
        const usuarioIds = [1, 2, 3, 4];
        for (const row of rows) {
            const destinoId = row.id;
            const fechaVisita = new Date().toISOString();
            const usuarioId = usuarioIds[Math.floor(Math.random() * usuarioIds.length)];
            await insertHistorial(usuarioId, destinoId, fechaVisita);
        }
    });
}

async function insertHistorial(usuarioId, destinoId, fechaVisita) {
    const sqlInsertarHistorial = `
        INSERT INTO Historial (usuarioId, destinoId, fechaVisita)
        VALUES (?, ?, ?)
    `;
    const params = [usuarioId, destinoId, fechaVisita];

    db.run(sqlInsertarHistorial, params, function (err) {
        if (err) {
            console.error('Error al insertar historial:', err.message);
            return;
        }
        // console.log(`Historial insertado con ID ${this.lastID}`);
    });
}

async function poblarFavoritos() {
    const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
    db.all(sqlSeleccionarDestinos, async (err, rows) => {
        if (err) {
            console.error('Error al seleccionar los destinos:', err.message);
            return;
        }
        const usuarioIds = [1, 2, 3, 4];
        for (const row of rows) {
            const destinoId = row.id;
            const usuarioId = usuarioIds[Math.floor(Math.random() * usuarioIds.length)];
            await insertFavorito(usuarioId, destinoId);
        }
    });
}

async function insertFavorito(usuarioId, destinoId) {
    const sqlInsertarFavorito = `
        INSERT INTO Favoritos (usuarioId, destinoId)
        VALUES (?, ?)
    `;
    const params = [usuarioId, destinoId];

    db.run(sqlInsertarFavorito, params, function (err) {
        if (err) {
            console.error('Error al insertar favorito:', err.message);
            return;
        }
        // console.log(`Favorito insertado con ID ${this.lastID}`);
    });
}








module.exports = CheckIfBDNull;