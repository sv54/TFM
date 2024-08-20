-- SQLite
-- SELECT * From Pais WHERE nombre == "Alemania" OR nombre == "España" OR nombre == "Ucrania" OR nombre == "Reino Unido" OR nombre == "Japón" OR nombre == "Australia" OR nombre == "Estados Unidos" OR nombre == "Canadá" OR nombre == "Turquía" OR nombre == "Corea del Sur" OR nombre == "Arabia Saudita"  OR nombre == "Tailandia" OR nombre == "Argentina";


-- DELETE From imgActividad;
-- SELECT * From imgDestino;
-- DELETE FROM Actividad;
-- Delete from Comentario;
-- Select * from Recomendacion;
SELECT * From Usuario;
SELECT * From Destino;
SELECT * From Comentario where destinoId = 5;

-- SELECT * From Destino;
-- SELECT * From Actividad;
-- SELECT * From ImgActividad;

-- SELECT COUNT(id) From Comentario where destinoId = 1;
--SELECT * FROM Favoritos where usuarioId == 4;
--SELECT * FROM Visitados where usuarioId == 4;
--SELECT * FROM Historial where usuarioId == 4;
-- SELECT * FROM Comentario where usuarioId == 4 and destinoId == 7;
SELECT * From Pais;
SELECT * From Historial;

SELECT actividadId, COUNT(*) AS totalRecomendaciones 
                      FROM Recomendacion 
                      WHERE actividadId IN (3, 11, 16)
                      GROUP BY actividadId;
UPDATE Destino SET gastoTotal = 2000 WHERE id = 1;
-- UPDATE Actividad SET numRecomendado = 143 WHERE id = 1;
-- UPDATE Actividad SET numRecomendado = 229 WHERE id = 2;
-- UPDATE Actividad SET numRecomendado = 291 WHERE id = 3;
-- UPDATE Actividad SET numRecomendado = 172 WHERE id = 4;
-- UPDATE Actividad SET numRecomendado = 222 WHERE id = 5;
-- UPDATE Actividad SET numRecomendado = 93 WHERE id = 6;
-- UPDATE Actividad SET numRecomendado = 130 WHERE id = 7;
-- UPDATE Actividad SET numRecomendado = 209 WHERE id = 8;
-- UPDATE Actividad SET numRecomendado = 166 WHERE id = 9;
-- UPDATE Actividad SET numRecomendado = 257 WHERE id = 10;
-- UPDATE Actividad SET numRecomendado = 249 WHERE id = 11;
-- UPDATE Actividad SET numRecomendado = 115 WHERE id = 12;
-- UPDATE Actividad SET numRecomendado = 260 WHERE id = 13;
-- UPDATE Actividad SET numRecomendado = 216 WHERE id = 14;
-- UPDATE Actividad SET numRecomendado = 168 WHERE id = 15;
-- UPDATE Actividad SET numRecomendado = 179 WHERE id = 16;
-- UPDATE Actividad SET numRecomendado = 159 WHERE id = 17;
-- UPDATE Actividad SET numRecomendado = 270 WHERE id = 18;
-- UPDATE Actividad SET numRecomendado = 174 WHERE id = 19;
-- UPDATE Actividad SET numRecomendado = 116 WHERE id = 20;
-- UPDATE Actividad SET numRecomendado = 202 WHERE id = 21;
-- UPDATE Actividad SET numRecomendado = 112 WHERE id = 22;
-- UPDATE Actividad SET numRecomendado = 267 WHERE id = 23;
-- UPDATE Actividad SET numRecomendado = 168 WHERE id = 24;
-- UPDATE Actividad SET numRecomendado = 297 WHERE id = 25;
-- UPDATE Actividad SET numRecomendado = 81 WHERE id = 26;



























-- ORDER BY
--     Historial.fechaEntrado;

-- Select * From Reporte;

-- DELETE FROM Historial where usuarioId == 4;

-- PRAGMA foreign_key_list(Comentario);