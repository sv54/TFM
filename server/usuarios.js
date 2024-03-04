const sqlite3 = require('sqlite3').verbose();
const express = require('express');
const router = express.Router();

const dbPath = 'TFMDB.db';
var db = new sqlite3.Database(dbPath);


// Definir rutas relacionadas con usuarios
router.get('/', (req, res) => {
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

router.post('/', (req, res) =>{
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


// Exportar el router para su uso en otros archivos
module.exports = router;