const sqlite3 = require('sqlite3').verbose();
const express = require('express');
const bcrypt = require('bcrypt')
const app = express();
const fs = require('fs');
const multer = require('multer');
const path = require('path');
const uuid = require('uuid')

const CheckIfBDNull = require('./seedsDB.js');

const dbPath = 'TFMDB.db';

const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/img/');
    },
    filename: function (req, file, cb) {
        const extention = path.extname(file.originalname);
        const filename = uuid.v4() + extention;
        cb(null, filename);
    }
});

const storageProfile = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/imgProfile/');
    },
    filename: function (req, file, cb) {
        const extention = path.extname(file.originalname);
        const filename = uuid.v4() + extention;
        cb(null, filename);
    }
});

const uploadPhoto = multer({ storage: storageProfile });
const upload = multer({ storage: storage });

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

app.post('/upload', upload.single('image'), (req, res) => {
    if (!req.file) {
      return res.status(400).send('No se ha subido ningún archivo');
    }
    
    res.status(200).send('Imagen subida correctamente');
  });

app.post('/restartDbData', async (req, res) => {
    try {

        const sqlScript = fs.readFileSync('./CreateDatabaseTFM.sql', 'utf-8');

        // Ejecutar el script SQL
        await new Promise((resolve, reject) => {
            db.exec(sqlScript, function (err) {
                if (err) {
                    reject(err);
                } else {
                    resolve();
                }
            });
        });

        db.close();
        db = new sqlite3.Database(dbPath);
        await CheckIfBDNull();



        res.status(200).json("Base de datos poblada con datos ejemplo");
    } catch (error) {
        console.error('Error al ejecutar el script SQL:', error.message);
        res.status(500).json({ error: 'Error del servidor al ejecutar el script SQL: ' + error.message });
    }
});

//Peticiones para Usuario

app.get('/usuario', (req, res) => {
    const sqlQuery = 'SELECT * FROM Usuario';

    db.all(sqlQuery, [], (err, rows) => {
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
        if (!rows) {
            console.log('No se encontró ningún usuario con el ID proporcionado:', id);
            res.status(404).send('No se encontró ningún usuario con el ID proporcionado');
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

    if (!userId || !newPassword) {
        res.status(400).json({ error: 'Se requiere el ID y la nueva contraseña en el cuerpo de la solicitud.' });
        return;
    }
    const selectQuery = 'SELECT password, salt FROM Usuario WHERE id = ?';
    db.get(selectQuery, userId, async (err, row) => {
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
        const hashedPassword = await bcrypt.hash(newPassword, salt)
        //console.log(currentPassword, salt)

        if (currentPassword !== hashedPassword) {
            // Actualizar la contraseña del usuario
            
            const updateQuery = 'UPDATE Usuario SET password = ? WHERE id = ?';
            db.run(updateQuery, [hashedPassword, userId], function (err) {
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

    if (!userId || !newCountryId) {
        res.status(400).json({ error: 'Se requiere el ID de usuario y el ID de país en el cuerpo de la solicitud.' });
        return;
    }

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


app.put('/usuario/changeFoto/:id', uploadPhoto.single('image'), (req, res) => {
    const id = req.params.id
    if (!id || !req.file) {
        res.status(400).json({ error: 'Se requiere el ID de usuario y la imagen nueva.' });
        return;
    }

    //obtener el path de la foto
    const newPhotoPath = req.file.filename;

    const updateQuery = 'UPDATE Usuario SET fotoPerfil = ? WHERE id = ?';
    db.run(updateQuery, [newPhotoPath, id], function (err) {
        if (err) {
            console.error('Error al cambiar la foto del usuario:', err.message);
            res.status(500).send('Error del servidor al cambiar la foto del usuario: ' + err.message);
            return;
        }
        res.send(`Foto del usuario con ID ${id} cambiado correctamente.`);
    });
});

app.get('/usuario/foto/:id', (req, res) => {
    const id = req.params.id;

    const getPhotoQuery = 'SELECT fotoPerfil FROM Usuario WHERE id = ?';
    db.get(getPhotoQuery, [id], (err, row) => {
        if (err) {
            console.error('Error al obtener la foto del usuario:', err.message);
            res.status(500).send('Error del servidor al obtener la foto del usuario: ' + err.message);
            return;
        }

        if (!row) {
            res.status(404).send('Foto del usuario no encontrada');
            return;
        }

        const photoFilename = row.fotoPerfil;
        const photoPath = path.join(__dirname, 'public', 'imgProfile', photoFilename);
        fs.access(photoPath, fs.constants.F_OK, (err) => {
            if (err) {
                console.error('Error al acceder al archivo de la foto:', err.message);
                res.status(404).send('Foto del usuario no encontrada');
                return;
            }

            res.sendFile(photoPath);
        });
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
                // TODO crear el token de sesion en la base de datos

            }
            else {
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
            if(!req.file){
                fotoPerfil = "ruta sin asignar"
            }
            else{

            }
            const sqlQuery = `INSERT INTO Usuario (nombre, email, password, salt, paisOrigen, metaViajes, fotoPerfil) VALUES (?, ?, ?, ?, ?, ?, ?)`;
            db.run(sqlQuery, [nombre, email, hashedPassword, salt, paisOrigen, metaViajes, fotoPerfil], function (err) {
                if (err) {
                    console.error('Error al insertar usuario:', err.message);
                    res.sendStatus(500).send('Error del servidor al crear usuario: ' + err.message)
                    return;
                }
                //TODO crear el token de sesion en la base de datos

                // Obtener el ID del usuario insertado
                lastId = this.lastID
                res.status(201).json({"lastId": this.lastID})
            });


        }
        else {
            res.status(409).send('El email ya esta registrado')
        }
    });


});


app.post('/signOut', (req, res) => {
    //TODO borrar el token de la base de datos?
});


//Peticiones para Destino

app.get('/destino', (req, res) => {
    const sqlQuery = 'SELECT * FROM Destino';

    db.all(sqlQuery, [], (err, rows) => {
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
        res.status(201).json({
            "lastId": this.lastID
        })
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