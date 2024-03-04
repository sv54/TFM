const sqlite3 = require('sqlite3').verbose();
const express = require('express');
const app = express();

const dbPath = 'TFMDB.db';
const router = express.Router();
app.use(express.json());
app.use('/users', router);
// Crear una instancia de la base de datos
var db = new sqlite3.Database(dbPath);

const { usuariosEjemplo, destinoEjemplo } = require('./seedsDB');



app.listen(3000, () => {
    console.log('Servidor iniciado en el puerto 3000');
});


function CheckIfBDNull() {
    const sqlQuery = 'SELECT COUNT(*) AS count FROM Destino';

    db.get(sqlQuery, (err, row) => {
        if (err) {
            console.error('Error al buscar destino:', err.message);
            return;
        }

        if (row.count == 0) {
            poblarDestino()
            poblarDBUsuarios()
        }
    });
}


function executeSelectQuery(sqlQuery, callback) {

    db.all(sqlQuery, (err, rows) => {
        if (err) {
            console.error('Error al ejecutar la consulta:', err.message);
            callback(err, null);
            return;
        }
        callback(null, rows);
    });
    db.close((err) => {
        if (err) {
            console.error('Error al cerrar la conexión:', err.message);
        }
        console.log('Conexión a la base de datos cerrada');
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
    db.close((err) => {
        if (err) {
            console.error('Error al cerrar la conexión:', err.message);
        }
        console.log('Conexión a la base de datos cerrada');
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
        //callback(null, this.lastID);
    });
}


router.get('/users/', (req, res) => {
    const sqlQuery = 'SELECT * FROM Usuario';

    db.all(sqlQuery, [], (err, rows) => {
        if (err) {
            console.error('Error al obtener destinos:', err.message);
            res.status(500).send('Error del servidor al obtener usuarios: ' + err.message)
            return;
        }
        res.json(rows)
    });
    // Lógica para manejar la solicitud GET a /users
});

router.post('/users/', (req, res) =>{
    const sqlQuery = `INSERT INTO Usuario (nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)`;
    // const { nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil } = req.body;
    console.log(req)
    db.run(sqlQuery, [nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil], function (err) {
        if (err) {
            console.error('Error al insertar usuario:', err.message);
            res.sendStatus(500).send('Error del servidor al crear usuario: ' + err.message)
            return;
        }
        // Obtener el ID del usuario insertado
        res.status(201).json(this.lastID)
    });
});

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


// CheckIfBDNull()
// getAllDestinos(ConsoleLog)
// getAllUsuarios(ConsoleLog)
//insertDestino(destinoEjemplo[0])

function poblarDBUsuarios() {
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

module.exports = db;
