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
SELECT * From Recomendacion;
SELECT * From Historial;
-- ORDER BY
--     Historial.fechaEntrado;

-- Select * From Reporte;

-- DELETE FROM Historial where usuarioId == 4;

PRAGMA foreign_key_list(Comentario);