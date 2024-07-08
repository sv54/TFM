const sqlite3 = require('sqlite3').verbose();
const express = require('express');
const bcrypt = require('bcrypt')
const app = express();
const fs = require('fs');
const multer = require('multer');
const path = require('path');
const uuid = require('uuid')

const CheckIfBDNull = require('./seedsDB.js');

const dbPath = './TFMDB.db';

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

const storageDestination = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/imgDestination/');
    },
    filename: function (req, file, cb) {
        const extention = path.extname(file.originalname);
        const filename = uuid.v4() + extention;
        cb(null, filename);
    }
});

const storageActivity = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/imgActivity/');
    },
    filename: function (req, file, cb) {
        const extention = path.extname(file.originalname);
        const filename = uuid.v4() + extention;
        cb(null, filename);
    }
});

const uploadProfile = multer({ storage: storageProfile });
const uploadDestination = multer({ storage: storageDestination });
const uploadActivity = multer({ storage: storageActivity });
const upload = multer({ storage: storage });

app.use(express.json());
app.use('/public', express.static(path.join(__dirname, 'public')));

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
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
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
        await sleep(3000)


        res.status(200).json("Base de datos poblada con datos ejemplo");
    } catch (error) {
        console.error('Error al ejecutar el script SQL:', error.message);
        res.status(500).json({ error: 'Error del servidor al ejecutar el script SQL: ' + error.message });
    }
});

//Peticiones para Usuario
// #region Usuario

app.get('/usuario', (req, res) => {
    const sqlQuery = 'SELECT * FROM Usuario';

    db.all(sqlQuery, [], (err, rows) => {
        if (err) {
            console.error('Error al obtener usuarios:', err.message);
            res.status(500).send('Error del servidor al obtener usuarios: ' + err.message)
            return;
        }

        res.status(200).json({ "usuarios": rows })
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


app.put('/usuario/changeFoto/:id', uploadProfile.single('image'), (req, res) => {
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

// #region Login


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

// #region Register


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
            if (!req.file) {
                fotoPerfil = "SinFoto"
            }
            else {

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
                res.status(201).json({ "lastId": this.lastID })
            });


        }
        else {
            res.status(409).send('El email ya esta registrado')
        }
    });


});

// #region SignOut TODO

app.post('/signOut', (req, res) => {
    //TODO borrar el token de la base de datos?
});







// #region Destino
//Peticiones para Destino

const { networkInterfaces } = require('os');

// Función para obtener la dirección IP local de la interfaz de ethernet
function getEthernetIpAddress() {
    const interfaces = networkInterfaces();
    let ethernetAddress = '127.0.0.1'; // Valor por defecto si no se encuentra una IP de ethernet

    // Priorizar la interfaz de ethernet
    for (const ifaceName in interfaces) {
        if (ifaceName.toLowerCase().includes('ethernet')) {
            const iface = interfaces[ifaceName];
            for (let i = 0; i < iface.length; i++) {
                const { address, family, internal } = iface[i];
                if (family === 'IPv4' && !internal) {
                    return address; // Devuelve la primera IP no interna de ethernet encontrada
                }
            }
        }
    }

    return ethernetAddress;
}

// Uso de la función para obtener la IP local de ethernet
const ethernetIpAddress = getEthernetIpAddress();
const baseDestinoUrl = 'http://' + ethernetIpAddress + ':3000/public/imgDestination/'


app.get('/destino', (req, res) => {
    const sqlQuery = `
        SELECT 
            Destino.*, 
            imgDestino.nombre AS imagen,
            Pais.nombre AS nombrePais
        FROM 
            Destino
        LEFT JOIN 
            imgDestino ON Destino.id = imgDestino.destinoId
        LEFT JOIN 
            Pais ON Destino.paisId = Pais.id
        GROUP BY 
            Destino.id;
    `;

    db.all(sqlQuery, [], (err, rows) => {
        if (err) {
            console.error('Error al obtener destinos:', err.message);
            res.status(500).send('Error del servidor al obtener destinos: ' + err.message);
            return;
        }

        // Añadir la URL completa de la imagen a cada destino
        const destinosConImagen = rows.map(row => {
            if (row.imagen) {
                row.imagen = baseDestinoUrl + row.imagen;
            }
            return row;
        });

        res.json(destinosConImagen);
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



app.post('/destino/addimage/:id', uploadDestination.single('image'), (req, res) => {
    const id = req.params.id
    if (!id || !req.file) {
        res.status(400).json({ error: 'Se requiere el ID de destino y la imagen a publicar.' });
        return;
    }

    //obtener el path de la imagen
    const newImagePath = req.file.filename;

    const insertQuery = 'INSERT INTO imgDestino (destinoId, nombre) VALUES (?, ?)';
    db.run(insertQuery, [id, newImagePath], function (err) {
        if (err) {
            console.error('Error al añadir la imagen al destino:', err.message);
            res.status(500).send('Error del servidor al añadir la imagen al destino: ' + err.message);
            return;
        }
        res.send(`Imagen del destino con ID ${id} añadida correctamente.`);
    });
});

app.get('/destino/images/:id', (req, res) => {
    const id = req.params.id;

    const getPhotoQuery = 'SELECT * FROM imgDestino WHERE destinoId = ?';
    db.all(getPhotoQuery, [id], (err, rows) => {
        if (err) {
            console.error('Error al obtener las fotos del destino:', err.message);
            res.status(500).send('Error del servidor al obtener las fotos del destino: ' + err.message);
            return;
        }

        if (!rows || rows.length == 0) {
            res.status(404).send('Fotos del destino no encontradas');
            return;
        }
        const nombresArchivos = rows.map(row => row.nombre);
        // console.log(nombresArchivos)
        res.status(200).json({
            "nombresArchivo": nombresArchivos
        })
    });
});

app.get('/destino/image/:imgName', (req, res) => {
    const fileName = req.params.imgName;

    const imgPath = path.join(__dirname, 'public', 'imgDestination', fileName);
    console.log(imgPath)
    res.sendFile(imgPath, (err) => {
        if (err) {
            console.error('Error al enviar el archivo:', err.message);
            res.status(404).send("Archivo no encontrado")
        }
    });
});

app.get('/buscarDestino', (req, res) => {
    const titulo = req.query.titulo;

    // Realizar la búsqueda en la base de datos
    const sqlQuery = `SELECT Destino.*, 
       imgDestino.nombre AS imagen,
       Pais.nombre AS nombrePais
        FROM Destino
        LEFT JOIN imgDestino ON Destino.id = imgDestino.destinoId
        LEFT JOIN Pais ON Destino.paisId = Pais.id
        WHERE titulo LIKE '%' || ? || '%'
        GROUP BY Destino.id;`;

    db.all(sqlQuery, [titulo], (err, rows) => {
        if (err) {
            console.error('Error al buscar destinos:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al buscar destinos.' });
        }
        const destinosConImagen = rows.map(row => {
            if (row.imagen) {
                row.imagen = baseDestinoUrl + row.imagen;
            }
            return row;
        });
        // Devolver los destinos encontrados en formato JSON
        res.status(200).json(rows);
    });
});

app.get('/destinoOrdenado', (req, res) => {
    const tipo = req.query.tipo;
    let orderByClause;
    switch (tipo) {
        case 'nombreAsc':
            orderByClause = 'ORDER BY Destino.titulo ASC';
            break;
        case 'nombreDesc':
            orderByClause = 'ORDER BY Destino.titulo DESC';
            break;
        case 'numVisitasAsc':
            orderByClause = 'ORDER BY Destino.numVisitas ASC';
            break;
        case 'numVisitasDesc':
            orderByClause = 'ORDER BY Destino.numVisitas DESC';
            break;
        case 'puntuacionAsc':
            orderByClause = 'ORDER BY (Destino.sumaPuntuaciones * 1.0 / Destino.numPuntuaciones) ASC';
            break;
        case 'puntuacionDesc':
            orderByClause = 'ORDER BY (Destino.sumaPuntuaciones * 1.0 / Destino.numPuntuaciones) DESC';
            break;
        default:
            return res.status(400).json({ error: 'Tipo de ordenación no válido' });
    }


    // Realizar la búsqueda en la base de datos
    const sqlQuery = `SELECT Destino.*, 
        imgDestino.nombre AS imagen,
        Pais.nombre AS nombrePais,
        CAST(sumaPuntuaciones AS FLOAT) / numPuntuaciones AS puntuacion
        FROM Destino
        LEFT JOIN imgDestino ON Destino.id = imgDestino.destinoId
        LEFT JOIN Pais ON Destino.paisId = Pais.id
        GROUP BY Destino.id
        ${orderByClause}`;

    db.all(sqlQuery, [], (err, rows) => {
        if (err) {
            console.error('Error al buscar destinos:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al buscar destinos.' });
        }
        const destinosConImagen = rows.map(row => {
            if (row.imagen) {
                row.imagen = baseDestinoUrl + row.imagen;
            }
            return row;
        });
        // Devolver los destinos encontrados en formato JSON
        res.status(200).json(rows);
    });
});

//_Actividades
// #region Actividad


app.get('/actividad', (req, res) => {
    const id = req.params.id
    const sqlQuery = 'SELECT * FROM Actividad';

    db.all(sqlQuery, id, (err, rows) => {
        if (err) {
            console.error('Error al obtener actividad:', err.message);
            res.status(500).send('Error del servidor al obtener actividad: ' + err.message)
            return;
        }
        res.json({ "actividades": rows })
    });
});

app.get('/actividad/:id', (req, res) => {
    const id = req.params.id
    const sqlQuery = 'SELECT * FROM Actividad WHERE id = ?';

    db.get(sqlQuery, id, (err, rows) => {
        if (err) {
            console.error('Error al obtener actividad:', err.message);
            res.status(500).send('Error del servidor al obtener actividad: ' + err.message)
            return;
        }
        res.json(rows)
    });
});


app.post('/actividad', (req, res) => {
    const sqlQuery = `INSERT INTO Actividad (titulo, descripcion, numRecomendado, destinoId) VALUES (?, ?, ?, ?)`;
    const { titulo, descripcion, numRecomendado, destinoId } = req.body;
    db.run(sqlQuery, [titulo, descripcion, numRecomendado, destinoId], function (err) {
        if (err) {
            console.error('Error al insertar actividad:', err.message);
            res.status(500).json({ error: 'Error del servidor al crear actividad: ' + err.message });
            return;
        }
        // Obtener el ID de la actividad insertada
        const actividadId = this.lastID;
        res.status(201).json({ "lastId": actividadId });
    });
});

app.delete('/actividad/:id', (req, res) => {
    const id = req.params.id
    const sqlQuery = 'DELETE FROM Actividad WHERE id == ?';
    db.run(sqlQuery, id, function (err) {
        if (err) {
            console.error('Error al eliminar actividad:', err.message);
            res.status(500).send('Error del servidor al eliminar actividad: ' + err.message);
            return;
        }
        res.send(`Actividad con ID ${id} eliminado correctamente.`);
    });
});


app.post('/actividad/addimage/:id', uploadActivity.single('image'), (req, res) => {
    const id = req.params.id
    if (!id || !req.file) {
        res.status(400).json({ error: 'Se requiere el ID de actividad y la imagen a publicar.' });
        return;
    }

    //obtener el path de la imagen
    const newImagePath = req.file.filename;

    const insertQuery = 'INSERT INTO imgActividad (ActividadId, nombre) VALUES (?, ?)';
    db.run(insertQuery, [id, newImagePath], function (err) {
        if (err) {
            console.error('Error al añadir la imagActividad:', err.message);
            res.status(500).send('Error del servidor al añadir la imagen al actividad: ' + err.message);
            return;
        }
        res.status(201).json({ "message": 'Imagen del actividad con ID: ' + id + 'añadida correctamente.' });
    });
});

app.get('/actividad/images/:id', (req, res) => {
    const id = req.params.id;

    const getPhotoQuery = 'SELECT * FROM imgActividad WHERE ActividadId = ?';
    db.all(getPhotoQuery, [id], (err, rows) => {
        if (err) {
            console.error('Error al obtener las fotos del actividad:', err.message);
            res.status(500).send('Error del servidor al obtener las fotos del actividad: ' + err.message);
            return;
        }

        if (!rows || rows.length == 0) {
            res.status(404).send('Fotos del actividad no encontradas');
            return;
        }
        const nombresArchivos = rows.map(row => row.nombre);
        //console.log(nombresArchivos)
        res.status(200).json({
            "nombresArchivo": nombresArchivos
        })
    });
});

app.get('/actividad/image/:imgName', (req, res) => {
    const fileName = req.params.imgName;

    const imgPath = path.join(__dirname, 'public', 'imgActivity', fileName);
    console.log(imgPath)
    res.sendFile(imgPath, (err) => {
        if (err) {
            console.error('Error al enviar el archivo:', err.message);
        }
    });
});

// #region Comentario


app.get('/destino/:id/comentario/:index', (req, res) => {
    const destinoId = req.params.id;
    const index = req.params.index;

    // Calcular el rango de comentarios a recuperar
    const offset = index * 10;
    const endIndex = 10;
    // console.log(endIndex)

    const sql = `
        SELECT *
        FROM Comentario
        WHERE destinoId = ? 
        LIMIT ?, ?
    `;

    // Ejecutar la consulta con los parámetros
    db.all(sql, [destinoId, offset, endIndex], (err, rows) => {
        if (err) {
            console.error('Error al obtener comentarios:', err.message);
            res.status(500).json({ error: 'Error al obtener comentarios' });
            return;
        }

        // Enviar los comentarios obtenidos como respuesta
        res.json({ comentarios: rows });
    });
});

app.get('/comentario/:id', async (req, res) => {
    const comentarioId = req.params.id;

    // Realizar la consulta SQL para obtener el comentario por su ID
    const sqlQuery = 'SELECT * FROM Comentario WHERE id = ?';

    db.get(sqlQuery, [comentarioId], (err, row) => {
        if (err) {
            console.error('Error al obtener el comentario:', err.message);
            res.status(500).send('Error interno del servidor');
            return;
        }

        if (!row) {
            res.status(404).send('Comentario no encontrado');
            return;
        }

        // Enviar el comentario encontrado como respuesta
        res.status(200).json(row);
    });
});


app.post('/comentario', (req, res) => {
    const { usuarioId, destinoId, texto, permisoExtraInfo, estanciaDias, dineroGastado, valoracion } = req.body;

    // Validar los datos recibidos
    if (!usuarioId || !destinoId || !valoracion) {
        return res.status(400).json({ error: 'Faltan campos obligatorios' });
    }

    // Consulta SQL para insertar el nuevo comentario en la base de datos
    const sql = `
        INSERT INTO Comentario (usuarioId, destinoId, texto, permisoExtraInfo, estanciaDias, dineroGastado, valoracion)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    `;

    // Ejecutar la consulta con los parámetros
    db.run(sql, [usuarioId, destinoId, texto, permisoExtraInfo, estanciaDias, dineroGastado, valoracion], function (err) {
        if (err) {
            console.error('Error al insertar comentario:', err.message);
            return res.status(500).json({ error: 'Error al insertar comentario' });
        }

        // Enviar la confirmación de que el comentario se ha creado correctamente
        res.status(201).json({ mensaje: 'Comentario creado exitosamente', comentarioId: this.lastID });
    });
});

app.delete('/comentario/:id', (req, res) => {
    const comentarioId = req.params.id;

    // Sentencia SQL para eliminar un comentario por su ID
    const sql = `DELETE FROM Comentario WHERE id = ?`;

    // Ejecutar la consulta SQL
    db.run(sql, [comentarioId], function (err) {
        if (err) {
            console.error('Error al eliminar el comentario:', err.message);
            return res.status(500).send('Error interno del servidor');
        }
        //console.log(`Comentario eliminado con ID ${comentarioId}`);
        res.status(200).send('Comentario eliminado correctamente');
    });
});

// #region Recomendacion


app.post('/actividad/:id/recomendar', (req, res) => {
    const actividadId = req.params.id;
    const usuarioId = req.body.usuarioId;

    if (!usuarioId) {
        return res.status(400).json({ error: 'ID de usuario no proporcionado en el cuerpo de la solicitud.' });
    }
    // Comprobar si el usuario existe en la base de datos
    const sqlCheckUser = 'SELECT id FROM Usuario WHERE id = ?';
    db.get(sqlCheckUser, [usuarioId], (err, row) => {
        if (err) {
            console.error('Error al comprobar la existencia del usuario:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al comprobar la existencia del usuario.' });
        }

        if (!row) {
            return res.status(404).json({ error: 'El usuario no existe.' });
        }
        // Insertar la recomendación en la base de datos
        const sqlQuery = 'INSERT INTO Recomendacion (usuarioId, actividadId) VALUES (?, ?)';
        db.run(sqlQuery, [usuarioId, actividadId], function (err) {
            if (err) {
                console.error('Error al recomendar la actividad:', err.message);
                return res.status(500).json({ error: 'Ocurrió un error al recomendar la actividad.' });
            }
            // Devolver una respuesta exitosa
            res.status(201).json({ message: 'Actividad recomendada exitosamente.' });
        });
    });
});

app.get('/actividad/:actividadId/recomendar', (req, res) => {
    const actividadId = req.params.actividadId;
    const sqlQuery = 'SELECT COUNT(*) AS totalRecomendaciones FROM Recomendacion WHERE actividadId = ?';
    db.get(sqlQuery, [actividadId], (err, row) => {
        if (err) {
            console.error('Error al obtener las recomendaciones de la actividad:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al obtener las recomendaciones de la actividad.' });
        }
        // Devolver la cantidad de recomendaciones encontradas
        const totalRecomendaciones = row.totalRecomendaciones || 0;
        res.status(200).json({ totalRecomendaciones });
    });
});

app.get('/actividad/recomendar', (req, res) => {
    console.log("Llamado")

    const actividadIds = req.query.ids;

    if (!actividadIds) {
        return res.status(400).json({ error: 'IDs de actividades no proporcionados.' });
    }

    // Convertir el parámetro de consulta en una lista de IDs
    const idsArray = actividadIds.split(',');

    // Crear placeholders para la consulta
    const placeholders = idsArray.map(() => '?').join(',');

    const sqlQuery = `SELECT actividadId, COUNT(*) AS totalRecomendaciones 
                      FROM Recomendacion 
                      WHERE actividadId IN (${placeholders})
                      GROUP BY actividadId`;

    db.all(sqlQuery, idsArray, (err, rows) => {
        if (err) {
            console.error('Error al obtener las recomendaciones de las actividades:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al obtener las recomendaciones de las actividades.' });
        }

        // Formatear el resultado en un objeto con los ID de actividad como claves
        const recomendaciones = {};
        rows.forEach(row => {
            recomendaciones[row.actividadId] = row.totalRecomendaciones;
        });

        res.status(200).json(recomendaciones);
    });
});

app.delete('/actividad/:id/recomendar', (req, res) => {
    const actividadId = req.params.id;
    const usuarioId = req.body.usuarioId;

    if (!usuarioId) {
        return res.status(400).json({ error: 'ID de usuario no proporcionado en el cuerpo de la solicitud.' });
    }
    // Eliminar la recomendación de la base de datos
    const sqlQuery = 'DELETE FROM Recomendacion WHERE usuarioId = ? AND actividadId = ?';
    db.run(sqlQuery, [usuarioId, actividadId], function (err) {
        if (err) {
            console.error('Error al eliminar la recomendación:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al eliminar la recomendación.' });
        }
        // Devolver una respuesta exitosa
        res.status(200).json({ message: 'Recomendación eliminada exitosamente.' });
    });
});


// #region Favoritos
app.post('/favoritos', (req, res) => {
    const { usuarioId, destinoId } = req.body;

    if (!usuarioId || !destinoId) {
        return res.status(400).json({ error: 'ID de usuario o destino no proporcionado en el cuerpo de la solicitud.' });
    }

    // Verificar si el usuario existe
    const sqlCheckUsuario = 'SELECT id FROM Usuario WHERE id = ?';
    db.get(sqlCheckUsuario, [usuarioId], (err, row) => {
        if (err) {
            console.error('Error al verificar el usuario:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al verificar el usuario.' });
        }
        if (!row) {
            return res.status(404).json({ error: 'Usuario no encontrado.' });
        }

        // Verificar si el destino existe
        const sqlCheckDestino = 'SELECT id FROM Destino WHERE id = ?';
        db.get(sqlCheckDestino, [destinoId], (err, row) => {
            if (err) {
                console.error('Error al verificar el destino:', err.message);
                return res.status(500).json({ error: 'Ocurrió un error al verificar el destino.' });
            }
            if (!row) {
                return res.status(404).json({ error: 'Destino no encontrado.' });
            }

            // Insertar la relación en la tabla Favoritos
            const sqlInsertFavorito = 'INSERT INTO Favoritos (usuarioId, destinoId) VALUES (?, ?)';
            db.run(sqlInsertFavorito, [usuarioId, destinoId], function (err) {
                if (err) {
                    console.error('Error al agregar el destino a los favoritos:', err.message);
                    return res.status(500).json({ error: 'Ocurrió un error al agregar el destino a los favoritos.' });
                }

                // Devolver una respuesta exitosa
                res.status(201).json({ message: 'Destino agregado a los favoritos exitosamente.' });
            });
        });
    });
});

app.get('/favoritos/:id', (req, res) => {
    const usuarioId = req.params.id;

    // Verificar si el usuario existe
    const sqlCheckUsuario = 'SELECT id FROM Usuario WHERE id = ?';
    db.get(sqlCheckUsuario, [usuarioId], (err, row) => {
        if (err) {
            console.error('Error al verificar el usuario:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al verificar el usuario.' });
        }
        if (!row) {
            return res.status(404).json({ error: 'Usuario no encontrado.' });
        }

        // Obtener todos los destinos favoritos del usuario junto con los detalles de esos destinos
        const sqlGetFavoritos = `
            SELECT Destino.*
            FROM Favoritos
            JOIN Destino ON Favoritos.destinoId = Destino.id
            WHERE Favoritos.usuarioId = ?
        `;
        db.all(sqlGetFavoritos, [usuarioId], (err, rows) => {
            if (err) {
                console.error('Error al obtener los destinos favoritos:', err.message);
                return res.status(500).json({ error: 'Ocurrió un error al obtener los destinos favoritos.' });
            }

            // Obtener los IDs de los destinos favoritos

            // Devolver los destinos favoritos junto con sus detalles completos
            res.status(200).json({ destinosFavoritos: rows });
        });
    });
});

// Ruta para eliminar un destino de los favoritos de un usuario
app.delete('/favoritos', (req, res) => {
    const { usuarioId, destinoId } = req.body;

    if (!usuarioId || !destinoId) {
        return res.status(400).json({ error: 'ID de usuario y destino son requeridos.' });
    }

    // Eliminar el destino de los favoritos del usuario
    const sqlQuery = 'DELETE FROM Favoritos WHERE usuarioId = ? AND destinoId = ?';
    db.run(sqlQuery, [usuarioId, destinoId], function (err) {
        if (err) {
            console.error('Error al eliminar el destino de los favoritos:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al eliminar el destino de los favoritos.' });
        }
        if (this.changes === 0) {
            return res.status(404).json({ error: 'No se encontró un favorito con el usuario y destino proporcionados.' });
        }
        // Devolver una respuesta exitosa
        res.status(200).json({ message: 'Destino eliminado de los favoritos exitosamente.' });
    });
});

//#region Visitados

app.post('/visitados', (req, res) => {
    const { usuarioId, destinoId, fechaMarcado } = req.body;

    if (!usuarioId || !destinoId || !fechaMarcado) {
        return res.status(400).json({ error: 'usuarioId, destinoId y fechaMarcado son requeridos.' });
    }

    // Comprobar si el usuario y el destino existen en la base de datos
    const sqlCheckUser = 'SELECT id FROM Usuario WHERE id = ?';
    db.get(sqlCheckUser, [usuarioId], (err, row) => {
        if (err) {
            console.error('Error al comprobar la existencia del usuario:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al comprobar la existencia del usuario.' });
        }
        if (!row) {
            return res.status(404).json({ error: 'El usuario no existe.' });
        }

        const sqlCheckDestino = 'SELECT id FROM Destino WHERE id = ?';
        db.get(sqlCheckDestino, [destinoId], (err, row) => {
            if (err) {
                console.error('Error al comprobar la existencia del destino:', err.message);
                return res.status(500).json({ error: 'Ocurrió un error al comprobar la existencia del destino.' });
            }
            if (!row) {
                return res.status(404).json({ error: 'El destino no existe.' });
            }

            // Insertar el lugar visitado en la base de datos
            const sqlInsertVisitado = 'INSERT INTO Visitados (usuarioId, destinoId, fechaMarcado) VALUES (?, ?, ?)';
            db.run(sqlInsertVisitado, [usuarioId, destinoId, fechaMarcado], function (err) {
                if (err) {
                    console.error('Error al guardar el lugar visitado:', err.message);
                    return res.status(500).json({ error: 'Ocurrió un error al guardar el lugar visitado.' });
                }
                // Incrementar el contador de visitas del destino
                const sqlUpdateDestino = 'UPDATE Destino SET numVisitas = numVisitas + 1 WHERE id = ?';
                db.run(sqlUpdateDestino, [destinoId], function (err) {
                    if (err) {
                        console.error('Error al actualizar el número de visitas del destino:', err.message);
                        return res.status(500).json({ error: 'Ocurrió un error al actualizar el número de visitas del destino.' });
                    }
                    res.status(201).json({ message: 'Lugar visitado guardado exitosamente.' });
                });
            });
        });
    });
});

app.get('/visitados/:id', (req, res) => {
    const usuarioId = req.params.id;

    // Consulta SQL para obtener los destinos visitados por el usuario
    const sqlQuery = `
        SELECT Destino.*
        FROM Visitados
        JOIN Destino ON Visitados.destinoId = Destino.id
        WHERE Visitados.usuarioId = ?
    `;

    db.all(sqlQuery, [usuarioId], (err, rows) => {
        if (err) {
            console.error('Error al obtener destinos visitados:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al obtener los destinos visitados.' });
        }

        // Destinos visitados
        res.status(200).json({ destinosVisitados: rows });
    });
});

app.delete('/visitados/', (req, res) => {
    const usuarioId = req.body.usuarioId;
    const destinoId = req.body.destinoId;

    if (!destinoId) {
        return res.status(400).json({ error: 'ID de destino no proporcionado en el cuerpo de la solicitud.' });
    }

    // Consulta SQL para eliminar el registro de Visitados por usuarioId y destinoId
    const sqlQuery = 'DELETE FROM Visitados WHERE usuarioId = ? AND destinoId = ?';

    db.run(sqlQuery, [usuarioId, destinoId], function (err) {
        if (err) {
            console.error('Error al eliminar el registro de visita:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al eliminar el registro de visita.' });
        }

        // Verificar si se eliminó correctamente
        if (this.changes === 0) {
            return res.status(404).json({ error: 'No se encontró ningún registro para eliminar.' });
        }

        // Éxito al eliminar el registro de visitados
        // Ahora actualizar el contador de visitas en Destino
        const sqlUpdateDestino = 'UPDATE Destino SET numVisitas = numVisitas - 1 WHERE id = ?';
        db.run(sqlUpdateDestino, [destinoId], function (err) {
            if (err) {
                console.error('Error al actualizar el número de visitas del destino:', err.message);
                return res.status(500).json({ error: 'Ocurrió un error al actualizar el número de visitas del destino.' });
            }
            res.status(200).json({ message: 'Registro de visita eliminado exitosamente.' });
        });
    });
});

// #region Historial 

app.get('/historial/:id', (req, res) => {
    const usuarioId = req.params.id;

    // Consulta SQL para obtener los destinos del historial del usuario
    const sqlQuery = `
        SELECT Destino.*, Historial.fechaVisita
        FROM Historial
        JOIN Destino ON Historial.destinoId = Destino.id
        WHERE Historial.usuarioId = ?
        ORDER BY Historial.fechaVisita DESC
    `;

    db.all(sqlQuery, [usuarioId], (err, rows) => {
        if (err) {
            console.error('Error al obtener el historial:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al obtener el historial.' });
        }

        // Devolver el historial encontrado
        res.status(200).json({ historial: rows });
    });
});

app.post('/historial', (req, res) => {
    const { usuarioId, destinoId } = req.body;
    const fechaVisita = new Date().toISOString();

    if (!usuarioId || !destinoId) {
        return res.status(400).json({ error: 'usuarioId y destinoId son requeridos.' });
    }

    // Comprobar si el usuario y el destino existen en la base de datos
    const sqlCheckUser = 'SELECT id FROM Usuario WHERE id = ?';
    db.get(sqlCheckUser, [usuarioId], (err, row) => {
        if (err) {
            console.error('Error al comprobar la existencia del usuario:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al comprobar la existencia del usuario.' });
        }
        if (!row) {
            return res.status(404).json({ error: 'El usuario no existe.' });
        }

        const sqlCheckDestino = 'SELECT id FROM Destino WHERE id = ?';
        db.get(sqlCheckDestino, [destinoId], (err, row) => {
            if (err) {
                console.error('Error al comprobar la existencia del destino:', err.message);
                return res.status(500).json({ error: 'Ocurrió un error al comprobar la existencia del destino.' });
            }
            if (!row) {
                return res.status(404).json({ error: 'El destino no existe.' });
            }

            // Insertar el destino en el historial
            const sqlInsertHistorial = 'INSERT INTO Historial (usuarioId, destinoId, fechaVisita) VALUES (?, ?, ?)';
            db.run(sqlInsertHistorial, [usuarioId, destinoId, fechaVisita], function (err) {
                if (err) {
                    console.error('Error al guardar en el historial:', err.message);
                    return res.status(500).json({ error: 'Ocurrió un error al guardar en el historial.' });
                }
                res.status(201).json({ message: 'Destino agregado al historial exitosamente.' });
            });
        });
    });
});

app.delete('/historial', (req, res) => {
    const { usuarioId, destinoId } = req.body;

    if (!usuarioId || !destinoId) {
        return res.status(400).json({ error: 'usuarioId y destinoId son requeridos.' });
    }

    // Consulta SQL para eliminar el registro del historial por usuarioId y destinoId
    const sqlQuery = 'DELETE FROM Historial WHERE usuarioId = ? AND destinoId = ?';

    db.run(sqlQuery, [usuarioId, destinoId], function (err) {
        if (err) {
            console.error('Error al eliminar el registro del historial:', err.message);
            return res.status(500).json({ error: 'Ocurrió un error al eliminar el registro del historial.' });
        }

        // Verificar si se eliminó correctamente
        if (this.changes === 0) {
            return res.status(404).json({ error: 'No se encontró ningún registro para eliminar.' });
        }

        // Éxito al eliminar el registro
        res.status(200).json({ message: 'Registro del historial eliminado exitosamente.' });
    });
});
