const sqlite3 = require('sqlite3').verbose();
const express = require('express');
const bcrypt = require('bcrypt')
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

async function getSalt() {
    const saltSerhii = bcrypt.genSalt(10);
    const salt = await saltSerhii;
    return salt;
}

async function getPass(contrasenya, salt) {
    pass = bcrypt.hash(contrasenya, salt)
    return pass;
}

//Peticiones para Usuario

app.get('/usuario', (req, res) => {
    const sqlQuery = 'SELECT * FROM Usuario';

    db.get(sqlQuery, [], (err, rows) => {
        if (err) {
            console.error('Error al obtener usuarios:', err.message);
            res.status(500).send('Error del servidor al obtener usuarios: ' + err.message)
            return;
        }
        res.json(rows)
    });
});


app.get('/usuario/:id', (req, res) => {
    const id = req.params.id
    const sqlQuery = 'SELECT * FROM Usuario WHERE id = ?';

    db.get(sqlQuery, id, (err, rows) => {
        if (err) {
            console.error('Error al obtener usuario:', err.message);
            res.status(500).send('Error del servidor al obtener usuario: ' + err.message)
            return;
        }
        res.json(rows)
    });
});

app.post('/usuario', (req, res) => {
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

app.delete('/usuario/:id', (req, res) => {
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

app.put('/usuario/changePassword', (req, res) => {
    const { userId, newPassword } = req.body;

    // Verificar si el ID y la nueva contraseña están presentes en el cuerpo de la solicitud
    if (!userId || !newPassword) {
        res.status(400).json({ error: 'Se requiere el ID y la nueva contraseña en el cuerpo de la solicitud.' });
        return;
    }
    const selectQuery = 'SELECT password, salt FROM Usuario WHERE id = ?';
    db.get(selectQuery, userId, (err, row) => {
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
            db.run(updateQuery, [newPassword, userId], function (err) {
                if (err) {
                    console.error('Error al actualizar la contraseña del usuario:', err.message);
                    res.status(500).send('Error del servidor al actualizar la contraseña del usuario: ' + err.message);
                    return;
                }
                res.send(`Contraseña del usuario con ID ${userId} actualizada correctamente.`);
            });
        } else {
            res.status(400).json({ error: 'La nueva contraseña no puede ser igual a la contraseña actual.' });
        }
    });
});

app.put('/usuario/changeCountry', (req, res) => {
    const { userId, newCountryId } = req.body;

    // Verificar si el ID de usuario y el ID de país están presentes en el cuerpo de la solicitud
    if (!userId || !newCountryId) {
        res.status(400).json({ error: 'Se requiere el ID de usuario y el ID de país en el cuerpo de la solicitud.' });
        return;
    }

    // Lógica para cambiar el país del usuario
    // Por ejemplo, podrías ejecutar una consulta SQL para actualizar el país del usuario en la base de datos
    const updateQuery = 'UPDATE Usuario SET paisOrigen = ? WHERE id = ?';
    db.run(updateQuery, [newCountryId, userId], function (err) {
        if (err) {
            console.error('Error al cambiar el país del usuario:', err.message);
            res.status(500).send('Error del servidor al cambiar el país del usuario: ' + err.message);
            return;
        }
        res.send(`País del usuario con ID ${userId} cambiado correctamente.`);
    });
});


app.post('/usuario/changeFoto', (req, res) => {
    const { userId, newPhoto } = req.body;

    // Verificar si el ID de usuario y el ID de país están presentes en el cuerpo de la solicitud
    if (!userId || !newPhoto) {
        res.status(400).json({ error: 'Se requiere el ID de usuario y la imagen nueva.' });
        return;
    }

    //Guardar y obtener el path de la foto aqui
    const newPhotoPath = ""
    const updateQuery = 'UPDATE Usuario SET fotoPerfil = ? WHERE id = ?';
    db.run(updateQuery, [newPhotoPath, userId], function (err) {
        if (err) {
            console.error('Error al cambiar la foto del usuario:', err.message);
            res.status(500).send('Error del servidor al cambiar la foto del usuario: ' + err.message);
            return;
        }
        res.send(`Foto del usuario con ID ${userId} cambiado correctamente.`);
    });
});

app.post('/login', (req, res) => {
    const { email, password } = req.body
    const sqlQuery = 'SELECT * FROM Usuario WHERE email = ? LIMIT 1';

    db.get(sqlQuery, email, async (err, rows) => {
        if (err) {
            console.error('Error al obtener usuario:', err.message);
            res.status(500).send('Error del servidor al obtener usuario: ' + err.message)
            return;
        }
        else if (rows == undefined || rows == null || rows.length === 0) {
            res.status(404).send('Usuario o contraseña son incorrectos.')
        }
        else {

            const hashedPassword = await bcrypt.hash(password, rows.salt)

            if (hashedPassword == rows.password) {
                console.log(rows)
                res.json(rows)
            }
            else{
                res.status(404).send('Usuario o contraseña son incorrectos.')
            }
        }
    });
});


app.post('/register', (req, res) => {
    const { nombre, email, password, paisOrigen, metaViajes } = req.body
    const sqlQuery = 'SELECT * FROM Usuario WHERE email = ? LIMIT 1';

    db.get(sqlQuery, email, async (err, rows) => {
        if (err) {
            console.error('Error al obtener usuario:', err.message);
            res.status(500).send('Error del servidor al obtener usuario: ' + err.message)
            return;
        }
        else if (rows == undefined || rows == null || rows.length === 0) {

            const salt = await bcrypt.genSalt(10)
            const hashedPassword = await bcrypt.hash(password, salt)
            fotoPerfil = "ruta sin asignar"
            const sqlQuery = `INSERT INTO Usuario (nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)`;
            db.run(sqlQuery, [nombre, email, hashedPassword, salt, paisOrigen, metaViajes, fotoPerfil], function (err) {
                if (err) {
                    console.error('Error al insertar usuario:', err.message);
                    res.sendStatus(500).send('Error del servidor al crear usuario: ' + err.message)
                    return;
                }
                // Obtener el ID del usuario insertado
                lastId = this.lastID
                res.status(201).json(this.lastID)
            });


        }
        else {
            res.status(409).send('El email ya esta registrado')
        }
    });


});


app.post('/signOut', (req, res) => {
    //Se borraria el token de la base de datos?
});


//Peticiones para Destino

app.get('/destino', (req, res) => {
    const sqlQuery = 'SELECT * FROM Destino';

    db.get(sqlQuery, [], (err, rows) => {
        if (err) {
            console.error('Error al obtener destino:', err.message);
            res.status(500).send('Error del servidor al obtener destino: ' + err.message)
            return;
        }
        res.json(rows)
    });
});


app.get('/destino/:id', (req, res) => {
    const id = req.params.id
    const sqlQuery = 'SELECT * FROM Destino WHERE id = ?';

    db.get(sqlQuery, id, (err, rows) => {
        if (err) {
            console.error('Error al obtener destino:', err.message);
            res.status(500).send('Error del servidor al obtener destino: ' + err.message)
            return;
        }
        res.json(rows)
    });
});


app.post('/destino', (req, res) => {
    const sqlQuery = `INSERT INTO Destino (titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`;
    const { titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima } = req.body;
    db.run(sqlQuery, [titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima], function (err) {
        if (err) {
            console.error('Error al insertar destino:', err.message);
            res.sendStatus(500).send('Error del servidor al crear destino: ' + err.message)
            return;
        }
        // Obtener el ID del destino insertado
        lastId = this.lastID
        res.status(201).json(this.lastID)
    });
});

app.delete('/destino/:id', (req, res) => {
    const id = req.params.id
    const sqlQuery = 'DELETE FROM Destino WHERE id == ?';
    db.run(sqlQuery, id, function (err) {
        if (err) {
            console.error('Error al eliminar destino:', err.message);
            res.status(500).send('Error del servidor al eliminar destino: ' + err.message);
            return;
        }
        res.send(`Destino con ID ${id} eliminado correctamente.`);
    });
});