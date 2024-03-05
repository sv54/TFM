
const usuariosEjemplo = [
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
		sumaPuntuaciones: 4500,
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
		sumaPuntuaciones: 6000,
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
		sumaPuntuaciones: 2800,
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
		sumaPuntuaciones: 8500,
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
		sumaPuntuaciones: 7500,
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
		sumaPuntuaciones: 5500,
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
		sumaPuntuaciones: 3500,
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
		sumaPuntuaciones: 9000,
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
		sumaPuntuaciones: 11000,
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
		sumaPuntuaciones: 8000,
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
		sumaPuntuaciones: 14000,
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
		sumaPuntuaciones: 7500,
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
		sumaPuntuaciones: 6500,
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
		sumaPuntuaciones: 5500,
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
		sumaPuntuaciones: 4500,
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
		sumaPuntuaciones: 6500,
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
		sumaPuntuaciones: 7500,
		gastoTotal: 1400,
		diasEstanciaTotal: 7,
		indiceSeguridad: 8,
		moneda: "Peso argentino",
		clima: "Subtropical"
	}
];

const sqlite3 = require('sqlite3').verbose();

const dbPath = 'TFMDB.db';
var db = new sqlite3.Database(dbPath);





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



CheckIfBDNull()
getAllDestinos(ConsoleLog)
getAllUsuarios(ConsoleLog)

function poblarUsuarios() {
	for (let i = 0; i < usuariosEjemplo.length; i++) {
		insertUsuario(usuariosEjemplo[i], ConsoleLog)
		console.log(usuariosEjemplo[i].nombre);
	}
}

function poblarDestino() {
	for (let i = 0; i < destinoEjemplo.length; i++) {
		insertDestino(destinoEjemplo[i], ConsoleLog)
		//console.log(destinoEjemplo[i].titulo);
	}
}

function ConsoleLog(mensaje, rows) {
	if (rows != null) {
		console.log('Callback:', rows);
	}
	else if (mensaje != null) {
		console.log('Callback:', mensaje);
	}
	else {
		console.log('Callback:', "NULL");
	}
}

function CheckIfBDNull() {
	const sqlQuery = 'SELECT COUNT(*) AS count FROM Destino';

	db.get(sqlQuery, (err, row) => {
		if (err) {
			console.error('Error al buscar destino:', err.message);
			return;
		}

		if (row.count == 0) {
			console.log("La base de datos esta vacia! Se rellena con datos ejemplo...")
			poblarDestino()
			poblarUsuarios()
		}
	});
}

function insertUsuario(usuarioData, callback) {
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

function insertDestino(destinoData, callback) {
    const sqlQuery = `INSERT INTO Destino (titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`;
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

module.exports = {
	usuariosEjemplo,
	destinoEjemplo
};