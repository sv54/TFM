const sqlite3 = require('sqlite3').verbose();
const express = require('express');
const app = express();

const dbPath = 'TFMDB.db';
const router = express.Router();
app.use(express.json());

// Crear una instancia de la base de datos
var db = new sqlite3.Database(dbPath);

var lastId


app.listen(3000, () => {
    console.log('Servidor iniciado en el puerto 3000');
});
app.get('/users', (req, res) => {
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

app.post('/users', (req, res) =>{
    const sqlQuery = `INSERT INTO Usuario (nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)`;
    const { nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil } = req.body;
    db.run(sqlQuery, [nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil], function (err) {
        if (err) {
            console.error('Error al insertar usuario:', err.message);
            res.sendStatus(500).send('Error del servidor al crear usuario: ' + err.message)
            return;
        }
        // Obtener el ID del usuario insertado
        lastId = this.lastID
        res.status(201).json(this.lastID)
    });
});

app.delete('/users/:id', (req, res) =>{
    const id = req.params.id
    const sqlQuery = 'DELETE FROM Usuario WHERE id == ?';
    db.run(sqlQuery, id, function (err) {
        if (err) {
            console.error('Error al eliminar usuario:', err.message);
            res.status(500).send('Error del servidor al eliminar usuario: ' + err.message);
            return;
        }
        res.send(`Usuario con ID ${id} eliminado correctamente.`);
    });
});

app.put('/users/changePassword', (req, res) => {
    const { id, newPassword } = req.body;

    // Verificar si el ID y la nueva contraseña están presentes en el cuerpo de la solicitud
    if (!id || !newPassword) {
        res.status(400).json({ error: 'Se requiere el ID y la nueva contraseña en el cuerpo de la solicitud.' });
        return;
    }
    const selectQuery = 'SELECT password, salt FROM Usuario WHERE id = ?';
    db.get(selectQuery, id, (err, row) => {
        if (err) {
            console.error('Error al consultar la contraseña del usuario:', err.message);
            res.status(500).send('Error del servidor al consultar la contraseña del usuario: ' + err.message);
            return;
        }

        // Verificar si el usuario existe
        if (!row) {
            res.status(404).json({ error: 'El usuario con el ID proporcionado no existe.' });
            return;
        }

        const currentPassword = row.password;
        const salt = row.salt;

        console.log(currentPassword, salt)

        //Hacer aqui conversion a hash de contraseña nueva con el salt

        // Verificar si la nueva contraseña es diferente de la contraseña actual
        if (currentPassword !== newPassword) {
            // Actualizar la contraseña del usuario
            const updateQuery = 'UPDATE Usuario SET password = ? WHERE id = ?';
            db.run(updateQuery, [newPassword, id], function(err) {
                if (err) {
                    console.error('Error al actualizar la contraseña del usuario:', err.message);
                    res.status(500).send('Error del servidor al actualizar la contraseña del usuario: ' + err.message);
                    return;
                }
                res.send(`Contraseña del usuario con ID ${id} actualizada correctamente.`);
            });
        } else {
            res.status(400).json({ error: 'La nueva contraseña no puede ser igual a la contraseña actual.' });
        }
    });
});
