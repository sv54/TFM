-- SQLite
-- SELECT * From Pais WHERE nombre == "Alemania" OR nombre == "España" OR nombre == "Ucrania" OR nombre == "Reino Unido" OR nombre == "Japón" OR nombre == "Australia" OR nombre == "Estados Unidos" OR nombre == "Canadá" OR nombre == "Turquía" OR nombre == "Corea del Sur" OR nombre == "Arabia Saudita"  OR nombre == "Tailandia" OR nombre == "Argentina";


-- DELETE From imgActividad;
-- SELECT * From imgDestino;
-- DELETE FROM Actividad;
-- Delete from Comentario;
-- Select * from Recomendacion;
SELECT * From Usuario;

SELECT * From Destino;
-- SELECT * From ImgActividad;

-- SELECT COUNT(id) From Comentario where destinoId = 1;
SELECT * FROM Favoritos where usuarioId == 4;
SELECT * FROM Visitados where usuarioId == 4;
SELECT * FROM Historial where usuarioId == 4;
-- DELETE FROM Historial where usuarioId == 4;