const bcrypt = require('bcrypt')

var usuariosEjemplo = [
	{
		nombre: 'Luis Gómez',
		email: 'luis@example.com',
		password: '123456',
		salt: 'salty123',
		paisOrigen: 211,
		metaViajes: 1,
		fotoPerfil: 'imgProfile1.png'
	},
	{
		nombre: 'María Fernández',
		email: 'maria@example.com',
		password: '123456',
		salt: 'salty123',
		paisOrigen: 211,
		metaViajes: 1,
		fotoPerfil: 'imgProfile2.jpeg'
	},
	{
		nombre: 'Carlos Rodríguez',
		email: 'carlos@example.com',
		password: '123456',
		salt: 'salty123',
		paisOrigen: 211,
		metaViajes: 1,
		fotoPerfil: 'imgProfile3.png'
	},
	{
		nombre: 'Ana Pérez',
		email: 'ana@example.com',
		password: '123456',
		salt: 'salty123',
		paisOrigen: 211,
		metaViajes: 1,
		fotoPerfil: 'imgProfile4.jpg'
	},
	{
		nombre: 'José Martínez',
		email: 'jose@example.com',
		password: '123456',
		salt: 'salty456',
		paisOrigen: 211,
		metaViajes: 2,
		fotoPerfil: 'imgProfile5.jpg'
	},
	{
		nombre: 'Laura Sánchez',
		email: 'laura@example.com',
		password: '123456',
		salt: 'salty789',
		paisOrigen: 211,
		metaViajes: 3,
		fotoPerfil: 'imgProfile6.png'
	}
];
// #region Destinos
const destinoEjemplo = [
	{
		titulo: "Madrid",
		descripcion: "Madrid, la capital española, es un crisol de historia, arte y energía cosmopolita. Sus amplias avenidas están bordeadas de elegantes edificios históricos y modernas estructuras, creando un paisaje urbano que mezcla lo antiguo con lo contemporáneo. La Puerta del Sol, centro neurálgico de la ciudad, late con la vida de los madrileños y turistas por igual, mientras que el imponente Palacio Real y los museos de renombre mundial como el Prado y el Reina Sofía atraen a amantes del arte de todo el mundo. Madrid es también conocida por su vida nocturna vibrante, con tabernas tradicionales, terrazas animadas y clubes nocturnos que dan vida a la ciudad hasta altas horas de la madrugada.",
		paisId: 211,
		numPuntuaciones: 500,
		sumaPuntuaciones: 2250,
		gastoTotal: 10000,
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
		sumaPuntuaciones: 3000,
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
		sumaPuntuaciones: 1400,
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
		sumaPuntuaciones: 4250,
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
		sumaPuntuaciones: 3750,
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
		sumaPuntuaciones: 2750,
		gastoTotal: 1100,
		diasEstanciaTotal: 4,
		indiceSeguridad: 8,
		moneda: "Euro",
		clima: "Templado"
	},
	{
		titulo: "Kiev",
		descripcion: "Kiev, la capital vibrante de Ucrania, se alza majestuosamente a lo largo de las curvas serenas del río Dniéper. Con una historia que se remonta a siglos atrás, sus calles empedradas y su arquitectura ecléctica cuentan historias de realeza y revoluciones. Desde la grandiosidad dorada de la Catedral de Santa Sofía hasta el imponente Monasterio de las Cuevas de Kiev, la ciudad respira una mezcla encantadora de tradición y modernidad. Sus bulliciosos mercados y cafeterías acogedoras ofrecen un refugio para los amantes de la cultura y la gastronomía, mientras que los parques expansivos invitan a los ciudadanos y visitantes a disfrutar de la naturaleza en el corazón de la metrópolis.",
		paisId: 235,
		numPuntuaciones: 400,
		sumaPuntuaciones: 1750,
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
		sumaPuntuaciones: 4500,
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
		sumaPuntuaciones: 5500,
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
		sumaPuntuaciones: 4000,
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
		sumaPuntuaciones: 7000,
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
		sumaPuntuaciones: 3750,
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
		sumaPuntuaciones: 3250,
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
		sumaPuntuaciones: 2750,
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
		sumaPuntuaciones: 2250,
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
		sumaPuntuaciones: 3750,
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
		sumaPuntuaciones: 3700,
		gastoTotal: 1400,
		diasEstanciaTotal: 7,
		indiceSeguridad: 8,
		moneda: "Peso argentino",
		clima: "Subtropical"
	}
];

const actividadesEjemplo = [
	// Actividades para Roma
	{
		titulo: "Coliseo",
		descripcion: "Descubre la historia del Coliseo Romano y su importancia en la antigua Roma.",
		numRecomendado: 0,
		destinoId: 1 // Suponiendo que el ID del destino de Roma es 1
	},
	{
		titulo: "Vaticano",
		descripcion: "Explora los museos del Vaticano y admira las obras maestras de artistas como Miguel Ángel y Rafael.",
		numRecomendado: 0,
		destinoId: 1
	},
	{
		titulo: "Foro Romano",
		descripcion: "Recorre las ruinas del Foro Romano y conoce más sobre la vida en la antigua Roma.",
		numRecomendado: 0,
		destinoId: 1
	},
	// Actividades para Kiev
	{
		titulo: "Monasterio de las Cuevas de Kiev",
		descripcion: "Explora este complejo monástico ortodoxo, un sitio declarado Patrimonio de la Humanidad por la UNESCO.",
		numRecomendado: 0,
		destinoId: 2 // Suponiendo que el ID del destino de Kiev es 2
	},
	{
		titulo: "Plaza de la Independencia",
		descripcion: "Disfruta de la arquitectura y la vida diaria en la plaza más grande de Kiev.",
		numRecomendado: 0,
		destinoId: 2
	},
	{
		titulo: "Museo Nacional de Historia de Ucrania",
		descripcion: "Descubre la historia y la cultura de Ucrania a través de las colecciones del museo.",
		numRecomendado: 0,
		destinoId: 2
	},
	// Actividades para Madrid
	{
		titulo: "Museo del Prado",
		descripcion: "Contempla obras maestras de artistas como Velázquez, Goya y El Greco en uno de los museos más importantes del mundo.",
		numRecomendado: 0,
		destinoId: 3 // Suponiendo que el ID del destino de Madrid es 3
	},
	{
		titulo: "Parque del Retiro",
		descripcion: "Relájate en este hermoso parque y disfruta de sus jardines, estanques y actividades al aire libre.",
		numRecomendado: 0,
		destinoId: 3
	},
	{
		titulo: "Palacio Real",
		descripcion: "Descubre la residencia oficial de la monarquía española y admira su impresionante arquitectura y decoración.",
		numRecomendado: 0,
		destinoId: 3
	},
	// Actividades para Alicante
	{
		titulo: "Castillo de Santa Bárbara",
		descripcion: "Contempla las impresionantes vistas de la ciudad y el mar Mediterráneo desde este castillo medieval.",
		numRecomendado: 0,
		destinoId: 4 // Suponiendo que el ID del destino de Alicante es 4
	},
	{
		titulo: "Casco Antiguo",
		descripcion: "Recorre las estrechas calles del casco antiguo y descubre la historia y la cultura de Alicante.",
		numRecomendado: 0,
		destinoId: 4
	},
	{
		titulo: "Playa del Postiguet",
		descripcion: "Disfruta del sol y el mar en esta popular playa urbana situada en el corazón de Alicante.",
		numRecomendado: 0,
		destinoId: 4
	},
	// Actividades para París
	{
		titulo: "Torre Eiffel",
		descripcion: "Contempla las impresionantes vistas de París desde la icónica Torre Eiffel.",
		numRecomendado: 0,
		destinoId: 5 // Suponiendo que el ID del destino de París es 5
	},
	{
		titulo: "Museo del Louvre",
		descripcion: "Explora una de las colecciones de arte más grandes del mundo en este famoso museo.",
		numRecomendado: 0,
		destinoId: 5
	},
	{
		titulo: "Montmartre",
		descripcion: "Descubre el encanto bohemio de este barrio histórico y visita la Basílica del Sagrado Corazón.",
		numRecomendado: 0,
		destinoId: 5
	},
	// Actividades para Londres
	{
		titulo: "Palacio de Buckingham",
		descripcion: "Observa el famoso cambio de guardia y admira la residencia oficial de la monarquía británica.",
		numRecomendado: 0,
		destinoId: 6 // Suponiendo que el ID del destino de Londres es 6
	},
	{
		titulo: "Museo Británico",
		descripcion: "Explora las colecciones del museo que abarcan miles de años de historia y cultura.",
		numRecomendado: 0,
		destinoId: 6
	},
	{
		titulo: "London Eye",
		descripcion: "Disfruta de vistas panorámicas de Londres desde esta famosa noria.",
		numRecomendado: 0,
		destinoId: 6
	},
	// Actividades para Tokio
	{
		titulo: "Templo Senso-ji",
		descripcion: "Explora el templo más antiguo de Tokio y admira su impresionante arquitectura y ambiente histórico.",
		numRecomendado: 0,
		destinoId: 7 // Suponiendo que el ID del destino de Tokio es 7
	},
	{
		titulo: "Shibuya",
		descripcion: "Sumérgete en la vibrante vida urbana de Tokio y experimenta la famosa intersección de Shibuya.",
		numRecomendado: 0,
		destinoId: 7
	},
	{
		titulo: "Palacio Imperial",
		descripcion: "Descubre la residencia oficial del Emperador de Japón y explora sus hermosos jardines.",
		numRecomendado: 0,
		destinoId: 7
	},
	// Actividades para Estambul
	{
		titulo: "Mezquita Azul",
		descripcion: "Admira la impresionante arquitectura y los hermosos azulejos de esta famosa mezquita.",
		numRecomendado: 0,
		destinoId: 8 // Suponiendo que el ID del destino de Estambul es 8
	},
	{
		titulo: "Gran Bazar",
		descripcion: "Explora uno de los mercados cubiertos más grandes del mundo y descubre una gran variedad de productos.",
		numRecomendado: 0,
		destinoId: 8
	},
	{
		titulo: "Palacio de Topkapi",
		descripcion: "Descubre la historia otomana y explora los exquisitos jardines y salones del palacio.",
		numRecomendado: 0,
		destinoId: 8
	},
	// Actividades para Berlín
	{
		titulo: "Muro de Berlín",
		descripcion: "Recorre el famoso Muro de Berlín y aprende sobre su importancia histórica en la ciudad.",
		numRecomendado: 0,
		destinoId: 9 // Suponiendo que el ID del destino de Berlín es 9
	},
	{
		titulo: "Museo de Pérgamo",
		descripcion: "Explora las impresionantes colecciones de arte y arqueología del museo.",
		numRecomendado: 0,
		destinoId: 9
	},
	{
		titulo: "Parque Tiergarten",
		descripcion: "Relájate en el parque más grande de Berlín y disfruta de la naturaleza y la tranquilidad.",
		numRecomendado: 0,
		destinoId: 9
	}
];

// #region Comentarios Textos
const textoPositivo = [
    "Me sentí renovado después de este viaje.",
    "Cada momento fue una nueva aventura.",
    "Volví con recuerdos inolvidables.",
    "Conocí gente maravillosa en el camino.",
    "Disfruté cada segundo del viaje.",
    "Fue una experiencia que superó mis expectativas.",
    "Todo salió perfecto durante el viaje.",
    "Me encantó descubrir cosas nuevas.",
    "Volví con una perspectiva renovada.",
    "El viaje fue una mezcla perfecta de relajación y emoción.",
    "Este viaje fue exactamente lo que necesitaba.",
    "Regresé a casa lleno de energía.",
    "Me llevé muchas enseñanzas de este viaje.",
    "El viaje fue mejor de lo que esperaba.",
    "Viví momentos muy especiales.",
    "El itinerario fue impecable.",
    "Tuve la oportunidad de desconectarme por completo.",
    "El viaje fue una experiencia enriquecedora.",
    "Disfruté de cada pequeño detalle.",
    "Fue un viaje lleno de emociones positivas.",
    "Me sorprendí gratamente con todo lo que viví.",
    "Volvería a hacerlo sin dudarlo.",
    "Me llevé recuerdos que atesoraré siempre.",
    "Fue un viaje que superó todas mis expectativas.",
    "Disfruté cada instante de esta experiencia.",
    "Me sentí conectado con el entorno.",
    "La organización del viaje fue excelente.",
    "Cada día trajo una nueva sorpresa.",
    "Me sentí pleno y satisfecho.",
    "El viaje me llenó de alegría.",
    "Fue una experiencia que recordaré toda la vida.",
    "Disfruté mucho explorando nuevos lugares.",
    "Volví con el corazón lleno de gratitud.",
    "Este viaje me dio una nueva perspectiva.",
    "Fue una experiencia transformadora.",
    "Cada día fue una aventura diferente.",
    "El viaje fue todo lo que había soñado.",
    "Me siento afortunado de haber vivido esto.",
    "Fue un viaje lleno de momentos inolvidables.",
    "Volví con una nueva energía.",
    "Disfruté de cada momento del viaje.",
    "Me llevé recuerdos increíbles.",
    "Fue un viaje lleno de sorpresas agradables.",
    "El viaje fue una experiencia muy enriquecedora.",
    "Me encantó cada parte de este viaje.",
    "Todo fue mejor de lo que imaginé.",
    "Volví sintiéndome completamente renovado.",
    "Este viaje fue una experiencia única.",
    "Me sentí completamente feliz durante el viaje.",
    "Fue una experiencia que me dejó sin palabras.",
    "El viaje me llenó de inspiración.",
	"El viaje fue una experiencia maravillosa, con muchas oportunidades para explorar y descubrir cosas nuevas. Aunque hubo algunos momentos desafiantes, en general, me sentí satisfecho con todo lo que viví.",
    "Me encantó la forma en que todo estuvo organizado, lo que hizo que cada día fuera una nueva aventura sin preocupaciones. Los lugares que visité fueron fascinantes y me dejaron con ganas de más.",
    "Este viaje superó mis expectativas en muchos aspectos. Pude disfrutar de momentos tranquilos y también de actividades emocionantes, lo que lo hizo muy equilibrado.",
    "La combinación de actividades y tiempo libre fue perfecta, permitiéndome disfrutar del viaje a mi propio ritmo. Me llevé muchos recuerdos que atesoraré por siempre.",
    "Cada día fue una sorpresa agradable, desde la comida hasta las vistas espectaculares. Este viaje fue justo lo que necesitaba para desconectar y recargar energías.",
    "El itinerario estuvo bien pensado, lo que me permitió aprovechar al máximo cada momento. Regresé a casa sintiéndome renovado y satisfecho con mi experiencia.",
    "Las experiencias que viví durante este viaje fueron únicas y emocionantes. Me encantó cada momento y me llevé muchos recuerdos especiales.",
    "Disfruté muchísimo de las actividades que hicimos, y la atención al detalle en la organización hizo que todo fuera muy fluido y agradable. Definitivamente recomendaría esta experiencia.",
    "Me encantó la mezcla de relajación y aventura que ofreció este viaje. Fue una experiencia bien equilibrada que me permitió disfrutar de lo mejor de ambos mundos.",
    "Este viaje fue una excelente manera de desconectar de la rutina y vivir nuevas experiencias. Pude explorar, aprender y relajarme en igual medida, lo cual fue perfecto.",
    "Me sorprendió gratamente la calidad de las actividades y las experiencias que tuvimos. Cada día fue una nueva aventura que disfruté al máximo.",
    "Este viaje fue una mezcla perfecta de cultura, naturaleza y descanso. Cada momento fue especial y me permitió desconectar completamente de la rutina.",
    "La organización del viaje fue impecable, lo que hizo que cada experiencia fuera aún más agradable. Disfruté mucho de todo lo que hice y de las personas que conocí en el camino.",
    "Fue un viaje increíblemente gratificante, con un equilibrio perfecto entre actividades y tiempo libre. Pude disfrutar del entorno y también relajarme por completo.",
    "Disfruté cada momento del viaje, desde los paisajes hasta las experiencias culturales. Fue una oportunidad única para desconectar y disfrutar al máximo.",
    "Me encantó la variedad de actividades y lugares que pude visitar durante este viaje. Cada día fue una nueva oportunidad para descubrir algo nuevo y emocionante.",
    "Este viaje fue exactamente lo que necesitaba para recargar energías. La combinación de aventura y relajación fue perfecta y regresé a casa sintiéndome renovado.",
    "Cada detalle estuvo bien pensado, lo que hizo que el viaje fuera muy fluido y agradable. Me llevé recuerdos increíbles y muchas experiencias enriquecedoras.",
    "Este viaje me ofreció justo la cantidad adecuada de aventura y descanso. Disfruté cada día al máximo y me llevé muchos recuerdos especiales a casa.",
    "La planificación del viaje fue excelente, lo que me permitió disfrutar sin preocupaciones. Cada actividad fue emocionante y me dejó con ganas de más.",
    "Disfruté mucho de la variedad de experiencias que ofreció este viaje. Pude explorar, aprender y relajarme, todo en un solo viaje.",
    "Este viaje fue una experiencia completa que combinó lo mejor de la aventura y la relajación. Regresé a casa sintiéndome pleno y satisfecho.",
    "La atención al detalle durante el viaje fue impresionante, lo que hizo que cada momento fuera especial. Me encantó cada parte de esta experiencia.",
    "Me sorprendió lo bien organizado que estuvo todo. Pude disfrutar de cada actividad y también tener tiempo para relajarme y disfrutar del entorno.",
    "Este viaje fue una combinación perfecta de emoción y tranquilidad. Cada día ofreció algo nuevo y emocionante, lo que hizo que la experiencia fuera inolvidable.",
    "Disfruté mucho de cada actividad y del tiempo que tuve para relajarme. Fue un viaje bien equilibrado que me permitió disfrutar al máximo.",
    "El viaje superó mis expectativas en todos los aspectos. Desde la organización hasta las actividades, todo fue perfecto y me permitió disfrutar al máximo.",
    "La variedad de experiencias que viví durante este viaje fue impresionante. Pude disfrutar de momentos de aventura, pero también de relajación y tranquilidad.",
    "Me encantó la forma en que el viaje estuvo organizado, lo que me permitió aprovechar al máximo cada momento. Volví a casa con muchos recuerdos especiales.",
    "Este viaje fue una excelente combinación de exploración y descanso. Pude disfrutar de lo mejor de ambos mundos y regresar a casa sintiéndome renovado.",
    "Cada actividad fue una nueva aventura que disfruté al máximo. Me encantó la diversidad de experiencias que viví durante este viaje.",
    "Disfruté muchísimo de cada momento del viaje. La combinación de actividades y tiempo libre fue perfecta, lo que hizo que la experiencia fuera inolvidable.",
    "La organización del viaje fue excelente, lo que me permitió disfrutar de cada actividad sin preocupaciones. Me llevé muchos recuerdos especiales.",
    "Este viaje fue una experiencia enriquecedora que me permitió desconectar y disfrutar de nuevas experiencias. Volvería a hacerlo sin dudarlo.",
    "Disfruté mucho de la diversidad de experiencias que ofreció este viaje. Cada día fue una nueva oportunidad para descubrir algo emocionante.",
    "Este viaje fue justo lo que necesitaba para recargar energías. La combinación de aventura y descanso fue perfecta y me permitió disfrutar al máximo.",
    "Cada detalle del viaje estuvo bien pensado, lo que hizo que la experiencia fuera muy agradable. Me llevé muchos recuerdos inolvidables a casa.",
    "Me encantó la variedad de actividades y lugares que pude explorar durante este viaje. Fue una experiencia completa y muy gratificante.",
    "Disfruté cada momento de este viaje, desde las actividades hasta los momentos de relajación. Fue una experiencia muy equilibrada y placentera.",
    "La planificación del viaje fue impecable, lo que me permitió disfrutar al máximo de cada actividad. Regresé a casa sintiéndome completamente satisfecho.",
    "Este viaje fue una excelente manera de desconectar de la rutina y vivir nuevas experiencias. Pude explorar, aprender y relajarme, todo en un solo viaje.",
    "Disfruté mucho de la combinación de aventura y descanso que ofreció este viaje. Fue una experiencia muy completa que me dejó con ganas de más.",
    "La atención al detalle durante el viaje fue impresionante, lo que hizo que cada momento fuera especial. Me encantó cada parte de esta experiencia.",
    "Este viaje superó mis expectativas en todos los aspectos. Desde la organización hasta las actividades, todo fue perfecto y me permitió disfrutar al máximo.",
    "Me encantó la variedad de actividades y experiencias que ofreció este viaje. Pude disfrutar de momentos de aventura y también de relajación.",
    "Disfruté mucho de cada actividad y del tiempo que tuve para relajarme. Fue un viaje bien equilibrado que me permitió disfrutar al máximo.",
    "Este viaje fue una combinación perfecta de emoción y tranquilidad. Cada día ofreció algo nuevo y emocionante, lo que hizo que la experiencia fuera inolvidable.",
    "La organización del viaje fue excelente, lo que me permitió disfrutar de cada actividad sin preocupaciones. Me llevé muchos recuerdos especiales.",
    "Cada día fue una sorpresa agradable, desde la comida hasta las vistas espectaculares. Este viaje fue justo lo que necesitaba para desconectar y recargar energías.",
    "Disfruté muchísimo de las actividades que hicimos, y la atención al detalle en la organización hizo que todo fuera muy fluido y agradable. Definitivamente recomendaría esta experiencia.",
];

const textoMediano = [
    "El viaje fue bastante agradable, aunque hubo algunos contratiempos.",
    "Disfruté de varias partes del viaje, pero no todo fue perfecto.",
    "Fue un viaje decente, pero me esperaba un poco más.",
    "Hubo momentos buenos, pero también algunos desafíos.",
    "El viaje cumplió su propósito, aunque no fue extraordinario.",
    "Fue una experiencia interesante, aunque no todo salió como esperaba.",
    "Tuve un tiempo aceptable, aunque podría haber sido mejor.",
    "El viaje fue satisfactorio en general, aunque algunos detalles no me convencieron.",
    "La experiencia fue buena, pero no tan memorable como esperaba.",
    "Hubo momentos agradables, pero también algunos inconvenientes.",
    "El viaje estuvo bien, aunque algunas cosas podrían mejorarse.",
    "Tuve una experiencia mixta, con aspectos buenos y otros no tanto.",
    "El viaje fue bueno, pero no superó mis expectativas.",
    "Disfruté algunas partes del viaje, aunque no todas.",
    "Fue un viaje aceptable, pero con algunos altibajos.",
    "El viaje fue bastante estándar, sin grandes sorpresas.",
    "Me gustó el viaje, aunque no todo salió según lo planeado.",
    "Hubo momentos destacables, pero también algunos problemas.",
    "El viaje estuvo bien, aunque no fue todo lo que esperaba.",
    "Fue una experiencia decente, pero no inolvidable.",
    "El viaje cumplió, pero me dejó con ganas de un poco más.",
    "Hubo partes buenas, pero también cosas que no me encantaron.",
    "El viaje fue agradable, aunque algunos detalles me decepcionaron.",
    "Fue un viaje correcto, pero con margen de mejora.",
    "La experiencia fue buena, aunque podría haber sido mejor.",
    "El viaje estuvo bien en general, aunque con algunas fallas.",
    "Me divertí en algunas partes del viaje, pero no en todas.",
    "El viaje fue razonable, pero no sin sus desafíos.",
    "Hubo cosas que disfruté, aunque otras no tanto.",
    "Fue un viaje aceptable, aunque me faltó algo más.",
    "La experiencia fue buena en general, aunque con altibajos.",
    "El viaje fue decente, pero no tan emocionante como esperaba.",
    "Tuve algunos buenos momentos, aunque no todo fue positivo.",
    "El viaje fue satisfactorio, pero no memorable.",
    "Hubo partes del viaje que me gustaron, pero otras no tanto.",
    "Fue una experiencia mixta, con puntos altos y bajos.",
    "El viaje fue aceptable, pero con algunos problemas.",
    "La experiencia fue correcta, aunque no excepcional.",
    "Tuve un tiempo agradable, aunque no sin inconvenientes.",
    "El viaje estuvo bien, aunque no fue espectacular.",
    "Disfruté de algunas partes, aunque otras no tanto.",
    "El viaje fue adecuado, pero podría haber sido mejor.",
    "Fue un viaje decente, pero no del todo satisfactorio.",
    "La experiencia fue buena, pero esperaba más.",
    "El viaje estuvo bien, aunque con algunos contratiempos.",
    "Disfruté del viaje en general, pero con algunas reservas.",
    "Fue una experiencia aceptable, aunque no sin sus fallas.",
    "El viaje fue razonable, pero no sin sus problemas.",
    "Me llevé buenos recuerdos, pero también algunos momentos difíciles.",
    "El viaje fue satisfactorio, aunque no perfecto.",
];



const comentariosEjemplo = [
	{
		"usuarioId": 1,
		"destinoId": "",
		"texto": "¡Qué lugar tan maravilloso! Disfruté mucho de mi visita.",
		"permisoExtraInfo": true,
		"estanciaDias": 5,
		"dineroGastado": 200,
		"valoracion": 5
	},
	{
		"usuarioId": 2,
		"destinoId": "",
		"texto": "¡Increíble experiencia! Recomiendo este lugar a todos.",
		"permisoExtraInfo": false,
		"estanciaDias": 3,
		"dineroGastado": 150,
		"valoracion": 4
	},
	{
		"usuarioId": 3,
		"destinoId": "",
		"texto": "Me encantó cada momento que pasé aquí. Volveré pronto.",
		"permisoExtraInfo": true,
		"estanciaDias": 7,
		"dineroGastado": 300,
		"valoracion": 5
	},
	{
		"usuarioId": 4,
		"destinoId": "",
		"texto": "El lugar es hermoso, pero la comida no me gustó tanto.",
		"permisoExtraInfo": false,
		"estanciaDias": 4,
		"dineroGastado": 180,
		"valoracion": 3
	},
	{
		"usuarioId": 1,
		"destinoId": "",
		"texto": "Experiencia única. Definitivamente regresaré en el futuro.",
		"permisoExtraInfo": true,
		"estanciaDias": 6,
		"dineroGastado": 250,
		"valoracion": 5
	},
	{
		"usuarioId": 2,
		"destinoId": "",
		"texto": "No puedo esperar para volver a este lugar. Me encantó todo.",
		"permisoExtraInfo": true,
		"estanciaDias": 4,
		"dineroGastado": 220,
		"valoracion": 5
	},
	{
		"usuarioId": 3,
		"destinoId": "",
		"texto": "Me sorprendió gratamente este destino. Lo recomiendo totalmente.",
		"permisoExtraInfo": true,
		"estanciaDias": 5,
		"dineroGastado": 190,
		"valoracion": 4
	},
	{
		"usuarioId": 4,
		"destinoId": "",
		"texto": "Buena experiencia en general. Me gustaría volver en el futuro.",
		"permisoExtraInfo": false,
		"estanciaDias": 3,
		"dineroGastado": 160,
		"valoracion": 4
	},
	{
		"usuarioId": 1,
		"destinoId": "",
		"texto": "El destino superó mis expectativas. Fue un viaje increíble.",
		"permisoExtraInfo": true,
		"estanciaDias": 7,
		"dineroGastado": 280,
		"valoracion": 5
	},
	{
		"usuarioId": 2,
		"destinoId": "",
		"texto": "¡Volveré sin duda alguna! Este lugar tiene tanto que ofrecer.",
		"permisoExtraInfo": true,
		"estanciaDias": 4,
		"dineroGastado": 230,
		"valoracion": 5
	},
	{
		"usuarioId": 3,
		"destinoId": "",
		"texto": "Me enamoré de este destino desde el primer día. Fue mágico.",
		"permisoExtraInfo": true,
		"estanciaDias": 6,
		"dineroGastado": 270,
		"valoracion": 5
	}
];
// #region Imgagenes
const nombresImgActividades = ['Buckingham1.jpg', 'Buckingham2.png', 'Buckingham3.jpg', 'CascoAntiguo1.jpg', 'CascoAntiguo2.jpg', 'CascoAntiguo3.jpg', 'CastilloDeSantaBarbara1.jpg', 'CastilloDeSantaBarbara2.jpg', 'CastilloDeSantaBarbara3.jpg', 'Coliseo1.jpg', 'Coliseo2.jpg', 'Coliseo3.jpeg', 'ForoRomano1.jpg', 'ForoRomano2.png', 'ForoRomano3.jpg', 'GranBazar1.jpg', 'GranBazar2.jpg', 'LondonEye1.JPG', 'LondonEye2.png', 'LondonEye3.jpg', 'Louvre1.png', 'Louvre2.png', 'Louvre3.jpeg', 'MezquitaAzul1.jpg', 'MezquitaAzul2.jpeg', 'MonasterioCuevasKiev1.jpg', 'MonasterioCuevasKiev2.jpg', 'Montmartre1.jpg', 'Montmartre2.JPG', 'Montmartre3.jpg', 'MuroDeBerlin1.jpg', 'MuroDeBerlin2.png', 'MuseoBritanico1.jpg', 'MuseoBritanico2.jpg', 'MuseoDelPrado1.jpg', 'MuseodelPrado2.png', 'MuseoDelPrado3.png', 'MuseoDePergamo1.jpg', 'MuseoDePergamo2.jpg', 'MuseoDePergamo3.jpeg', 'MuseoNacionalDeHistoriaDeUcrania1.jpg', 'MuseoNacionalDeHistoriaDeUcrania2.jpg', 'MuseoNacionalDeHistoriaDeUcrania3.jpg', 'PalacioImperial1.jpg', 'PalacioImperial2.jpg', 'PalacioImperial3.jpg', 'PalacioReal1.jpg', 'PalacioReal2.jpg', 'PalacioReal3.jpg', 'PalacioTopkapi1.jpg', 'PalacioTopkapi2.jpg', 'PalacioTopkapi3.jpg', 'ParqueDelRetiro1.jpg', 'ParqueDelRetiro2.png', 'ParqueDelRetiro3.png', 'PlayaDelPostiguet1.jpg', 'PlayaDelPostiguet2.jpg', 'PlazaDeLaIndependencia1.jpg', 'PlazaDeLaIndependencia2.jpg', 'Senso-ji1.jpg', 'Senso-ji2.jpg', 'Senso-ji3.jpg', 'Shibuya1.jpg', 'Shibuya2.png', 'Shibuya3.jpg', 'TorreEiffel1.jpg', 'TorreEiffel2.jpg', 'TorreEiffel3.png', 'Vaticano1.png', 'Vaticano2.jpg'];

const nombresImgDestinos = ['Alicante1.jpg', 'Alicante2.png', 'Bangkok1.jpg', 'Bangkok2.jpg', 'Bangkok3.jpg', 'Barcelona1.jpg', 'Barcelona2.jpg', 'Barcelona3.png', 'Berlín1.jpg', 'Berlín2.png', 'Berlín3.png', 'Berlín4.jpg', 'Buenos Aires1.png', 'Buenos Aires2.jpeg', 'Buenos Aires3.jpg', 'Dubai1.jpg', 'Dubai2.png', 'Estambul1.png', 'Estambul2.jpeg', 'Kiev1.jpg', 'Kiev2.jpg', 'Kiev3.jpg', 'Londres1.jpg', 'Londres2.jpg', 'Londres3.png', 'Madrid1.png', 'Madrid2.png', 'Madrid3.jpg', 'Nueva York1.jpg', 'Nueva York2.jpg', 'París1.jpg', 'París2.png', 'París3.jpg', 'Roma1.jpg', 'Roma2.jpg', 'Roma3.jpg', 'Seúl1.jpg', 'Seúl2.jpg', 'Seúl3.jpg', 'Sidney1.png', 'Sidney2.jpg', 'Sidney3.png', 'Tokio1.jpg', 'Tokio2.jpg', 'Tokio3.jpg', 'Toronto1.png', 'Toronto2.jpg', 'Toronto3.png'];




const usuarioSerhii = {
	nombre: 'Serhii',
	email: 'serhii@example.com',
	password: '123456',
	salt: null,
	paisOrigen: 211,
	metaViajes: 3,
	fotoPerfil: 'serhii.png'
}

const sqlite3 = require('sqlite3').verbose();

const dbPath = 'TFMDB.db';
var db = new sqlite3.Database(dbPath);



async function getSalt() {
	const saltSerhii = bcrypt.genSalt(10);
	const salt = await saltSerhii;
	return salt;
}

async function getPass(contrasenya, salt) {

	pass = bcrypt.hash(contrasenya, salt)
	return pass;
}

async function insertarSerhii() {
	const salt = await getSalt()
	const pass = await getPass(usuarioSerhii.password, salt)

	usuarioSerhii.salt = salt
	usuarioSerhii.password = pass

	insertUsuario(usuarioSerhii, ConsoleLog)
}

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

//PoblarComentarioNuevo()

async function poblarUsuarios() {
	for (let i = 0; i < usuariosEjemplo.length; i++) {
		await insertUsuario(usuariosEjemplo[i], ConsoleLog)
		// console.log(usuariosEjemplo[i].nombre);
	}
}

async function poblarDestino() {
	for (let i = 0; i < destinoEjemplo.length; i++) {
		await insertDestino(destinoEjemplo[i], ConsoleLog)
		//console.log(destinoEjemplo[i].titulo);
	}
}

function ConsoleLog(mensaje, rows) {
	if (rows != null) {
		// console.log('Callback:', rows);
	}
	else if (mensaje != null) {
		console.log('Callback:', mensaje);
	}
	else {
		// console.log('Callback:', "NULL");
	}
}
// #region Main Function
async function CheckIfBDNull() {
	const sqlQuery = 'SELECT COUNT(*) AS count FROM Destino';

	db.get(sqlQuery, async (err, row) => {
		if (err) {
			console.error('Error al buscar destino:', err.message);
			return;
		}
		if (row.count == 0) {
			console.log("La base de datos esta vacia! Se rellena con datos ejemplo...")
			await poblarDestino()
			await sleep(1000)
			await poblarActividad()
			await sleep(1000)
			await poblarUsuarios()
			await sleep(1000)
			await poblarImgDestino()
			await poblarImgActividad(ConsoleLog)
			await poblarComentarios();
			await poblarVisitados();
			await poblarHistoria();
			await poblarFavoritos();

			//insertarSerhii()


		}
	});
}

async function insertUsuario(usuarioData, callback) {
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

async function insertDestino(destinoData, callback) {
	const sqlQuery = `INSERT INTO Destino (titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima, numVisitas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, `+ generarNumeroAleatorio(1,1150) +`)`;
	const { titulo, descripcion, paisId, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima } = destinoData;
	const numPuntuaciones = generarNumeroAleatorio(1, 100);
	const sumaPuntuaciones = generarNumeroAleatorio(numPuntuaciones, numPuntuaciones * 5);
	db.run(sqlQuery, [titulo, descripcion, paisId, numPuntuaciones, sumaPuntuaciones, gastoTotal, diasEstanciaTotal, indiceSeguridad, moneda, clima], function (err) {
		if (err) {
			console.error('Error al insertar destino:', err.message);
			callback(err, null);
			return;
		}
		//console.log(descripcion)
		// Obtener el ID del destino insertado
		callback(null, this.lastID);
	});
}
function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}
async function insertActividad(actividadData, destinoName, callback) {
	// Primero, consulta el ID del destino utilizando el nombre proporcionado
	const sqlSelectDestinoId = `SELECT id FROM Destino WHERE titulo = ?`;
	db.get(sqlSelectDestinoId, [destinoName], (err, row) => {
		if (err) {
			console.error('Error al obtener el ID del destino:', err.message);
			callback(err, null);
			return;
		}
		if (!row) {
			// Si no se encuentra ningún destino con el nombre proporcionado
			const error = new Error('No se encontró ningún destino con el nombre proporcionado: ' + destinoName + ";");
			callback(error, null);
			return;
		}
		const destinoId = row.id;

		// Insertar la actividad utilizando el ID del destino obtenido
		const sqlQuery = `INSERT INTO Actividad (titulo, descripcion, numRecomendado, destinoId) VALUES (?, ?, ?, ?)`;
		const { titulo, descripcion, numRecomendado } = actividadData;
		db.run(sqlQuery, [titulo, descripcion, numRecomendado, destinoId], function (err) {
			if (err) {
				console.error('Error al insertar actividad:', err.message);
				callback(err, null);
				return;
			}
			// Obtener el ID de la actividad insertada
			callback(null, this.lastID);
		});
	});
}
async function poblarActividad() {
	for (var i = 0; i < actividadesEjemplo.length; i++) {
		var titulo = ""
		if (i >= 0 && i <= 2) {
			titulo = "Roma"
		}
		else if (i >= 3 && i <= 5) {
			titulo = "Kiev"
		}
		else if (i >= 6 && i <= 8) {
			titulo = "Madrid"
		}
		else if (i >= 9 && i <= 11) {
			titulo = "Alicante"
		}
		else if (i >= 12 && i <= 14) {
			titulo = "París"
		}
		else if (i >= 15 && i <= 17) {
			titulo = "Londres"
		}
		else if (i >= 18 && i <= 20) {
			titulo = "Tokio"
		}
		else if (i >= 21 && i <= 22) {
			titulo = "Estambul"
		}
		else if (i >= 23 && i <= 25) {
			titulo = "Berlín"
		}
		await insertActividad(actividadesEjemplo[i], titulo, ConsoleLog)
	}
}

async function RevisarNombresDestinos() {
	var nombreDestino = '';
	var contador = 0;

	for (var i = 0; i < nombresImgDestinos.length; i++) {
		nombreDestino = quitarExtension(nombresImgDestinos[i]);

		try {
			const row = await obtenerIdDestinoAsync(nombreDestino);
			if (row) {
				contador++;
			}
		} catch (err) {
			console.error('Error al obtener el ID del destino:', err.message);
		}
	}

	console.log("correctos: " + contador);
}

async function obtenerIdDestinoAsync(nombreDestino) {
	return new Promise((resolve, reject) => {
		const sqlSelectDestinoId = `SELECT id FROM Destino WHERE titulo = ?`;
		db.get(sqlSelectDestinoId, [nombreDestino], (err, row) => {
			if (err) {
				reject(err);
			} else {
				resolve(row);
			}
		});
	});
}

function quitarExtension(nombreArchivo) {
	const partes = nombreArchivo.split('.');
	partes.pop();
	var nombreSinExtension = partes.join('');
	nombre = nombreSinExtension.replace(/\d/g, '');
	return nombre;
}

async function insertarImgDestino(destinoName, nombreFichero, callback) {
	const sqlSelectDestinoId = `SELECT id FROM Destino WHERE titulo = ?`;
	db.get(sqlSelectDestinoId, [destinoName], (err, row) => {
		if (err) {
			console.error('Error al obtener el ID del destino:', err.message);
			callback(err, null);
			return;
		}
		if (!row) {
			// Si no se encuentra ningún destino con el nombre proporcionado
			const error = new Error('No se encontró ningún destino con el nombre proporcionado: ' + destinoName + ";");
			callback(error, null);
			return;
		}
		const destinoId = row.id;
		nombreFichero = nombreFichero.replace(/\s/g, "");
		// Insertar la actividad utilizando el ID del destino obtenido
		const sqlQuery = `INSERT INTO imgDestino (destinoId, nombre) VALUES (?, ?)`;

		db.run(sqlQuery, [destinoId, nombreFichero], function (err) {
			if (err) {
				console.error('Error al insertar actividad:', err.message);
				callback(err, null);
				return;
			}
			// Obtener el ID de la actividad insertada
			callback(null, this.lastID);
		});
	});
}
async function poblarImgDestino() {
	var nombreDestino = ""
	for (var i = 0; i < nombresImgDestinos.length; i++) {
		nombreDestino = quitarExtension(nombresImgDestinos[i])
		await insertarImgDestino(nombreDestino, nombresImgDestinos[i], ConsoleLog)
	}
}

async function poblarImgActividad(callback) {
	const sqlAct = `SELECT id, titulo FROM Actividad`;
	db.all(sqlAct, async (err, rows) => {
		if (err) {
			console.error('Error al obtener actividades', err.message);
			callback(err, null);
			return;
		}
		if (!rows) {
			const error = new Error('No se encontró ningúna activdad');
			callback(error, null);
			return;
		}
		else {
			var img1 = "", img2 = "", img3 = "";
			for (const row of rows) {
				img1 = "";
				img2 = "";
				img3 = "";
				if (row.titulo == "Coliseo") {
					img1 = "Coliseo1.jpg";
					img2 = "Coliseo2.jpg";
					img3 = "Coliseo3.jpeg";
				} else if (row.titulo == "Vaticano") {
					img1 = "Vaticano1.png";
					img2 = "Vaticano2.jpg";
					img3 = "";
				} else if (row.titulo == "Museo del Prado") {
					img1 = "MuseoDelPrado1.jpg";
					img2 = "MuseodelPrado2.png";
					img3 = "MuseoDelPrado3.png";
				} else if (row.titulo == "Parque del Retiro") {
					img1 = "ParqueDelRetiro1.jpg";
					img2 = "ParqueDelRetiro2.png";
					img3 = "ParqueDelRetiro3.png";
				} else if (row.titulo == "Palacio Real") {
					img1 = "PalacioReal1.jpg";
					img2 = "PalacioReal2.jpg";
					img3 = "PalacioReal3.jpg";
				} else if (row.titulo == "Castillo de Santa Bárbara") {
					img1 = "CastilloDeSantaBarbara1.jpg";
					img2 = "CastilloDeSantaBarbara2.jpg";
					img3 = "CastilloDeSantaBarbara3.jpg";
				} else if (row.titulo == "Plaza de la Independencia") {
					img1 = "PlazaDeLaIndependencia1.jpg";
					img2 = "PlazaDeLaIndependencia2.jpg";
					img3 = "";
				} else if (row.titulo == "Torre Eiffel") {
					img1 = "TorreEiffel1.jpg";
					img2 = "TorreEiffel2.jpg";
					img3 = "TorreEiffel3.png";
				} else if (row.titulo == "Museo del Louvre") {
					img1 = "Louvre1.png";
					img2 = "Louvre2.png";
					img3 = "Louvre3.jpeg";
				} else if (row.titulo == "Montmartre") {
					img1 = "Montmartre1.jpg";
					img2 = "Montmartre2.JPG";
					img3 = "Montmartre3.jpg";
				} else if (row.titulo == "Palacio de Buckingham") {
					img1 = "Buckingham1.jpg";
					img2 = "Buckingham2.png";
					img3 = "Buckingham3.jpg";
				} else if (row.titulo == "Museo Británico") {
					img1 = "MuseoBritanico1.jpg";
					img2 = "MuseoBritanico2.jpg";
					img3 = "";
				} else if (row.titulo == "London Eye") {
					img1 = "LondonEye1.JPG";
					img2 = "LondonEye2.png";
					img3 = "LondonEye3.jpg";
				} else if (row.titulo == "Casco Antiguo") {
					img1 = "CascoAntiguo1.jpg";
					img2 = "CascoAntiguo2.jpg";
					img3 = "CascoAntiguo3.jpg";
				} else if (row.titulo == "Shibuya") {
					img1 = "Shibuya1.jpg";
					img2 = "Shibuya2.png";
					img3 = "Shibuya3.jpg";
				} else if (row.titulo == "Palacio Imperial") {
					img1 = "PalacioImperial1.jpg";
					img2 = "PalacioImperial2.jpg";
					img3 = "PalacioImperial3.jpg";
				} else if (row.titulo == "Mezquita Azul") {
					img1 = "MezquitaAzul1.jpg";
					img2 = "MezquitaAzul2.jpeg";
					img3 = "";
				} else if (row.titulo == "Gran Bazar") {
					img1 = "GranBazar1.jpg";
					img2 = "GranBazar2.jpg";
					img3 = "";
				} else if (row.titulo == "Monasterio de las Cuevas de Kiev") {
					img1 = "MonasterioCuevasKiev1.jpg";
					img2 = "MonasterioCuevasKiev2.jpg";
					img3 = "";
				} else if (row.titulo == "Foro Romano") {
					img1 = "ForoRomano1.jpg";
					img2 = "ForoRomano2.png";
					img3 = "ForoRomano3.jpg";
				} else if (row.titulo == "Museo Nacional de Historia de Ucrania") {
					img1 = "MuseoNacionalDeHistoriaDeUcrania1.jpg";
					img2 = "MuseoNacionalDeHistoriaDeUcrania2.jpg";
					img3 = "MuseoNacionalDeHistoriaDeUcrania3.jpg";
				} else if (row.titulo == "Templo Senso-ji") {
					img1 = "Senso-ji1.jpg";
					img2 = "Senso-ji2.jpg";
					img3 = "Senso-ji3.jpg";
				} else if (row.titulo == "Playa del Postiguet") {
					img1 = "PlayaDelPostiguet1.jpg";
					img2 = "PlayaDelPostiguet2.jpg";
					img3 = "";
				} else if (row.titulo == "Palacio de Topkapi") {
					img1 = "PalacioTopkapi1.jpg";
					img2 = "PalacioTopkapi2.jpg";
					img3 = "PalacioTopkapi3.jpg";
				} else if (row.titulo == "Muro de Berlín") {
					img1 = "MuroDeBerlin1.jpg";
					img2 = "MuroDeBerlin2.png";
					img3 = "";
				} else if (row.titulo == "Museo de Pérgamo") {
					img1 = "MuseoDePergamo1.jpg";
					img2 = "MuseoDePergamo2.jpg";
					img3 = "MuseoDePergamo3.jpeg";
				}

				await insertarImgActividad(row.id, img1, ConsoleLog);
				await insertarImgActividad(row.id, img2, ConsoleLog);
				if (img3 != "") {
					await insertarImgActividad(row.id, img3, ConsoleLog)
				}
			}
		}


	});
}

async function insertarImgActividad(actividadId, nombre, callback) {
	const sqlQuery = `INSERT INTO imgActividad (actividadId, nombre) VALUES (?, ?)`;
	db.run(sqlQuery, [actividadId, nombre], function (err) {
		if (err) {
			console.error('Error al insertar imagen de actividad:', err.message, "::::" + " " + nombre + " ::: " + actividadId);
			callback(err, null);
			return;
		}
		// Obtener el ID de la actividad insertada
		callback(null, this.lastID);

	});
}

async function insertComentario(destinoId, comentario) {
	const sqlInsertarComentario = `
	INSERT INTO Comentario (usuarioId, destinoId, texto, permisoExtraInfo, estanciaDias, dineroGastado, valoracion)
	VALUES (?, ?, ?, ?, ?, ?, ?)
	`;
	const params = [
		comentario.usuarioId,
		destinoId,
		comentario.texto,
		comentario.permisoExtraInfo,
		comentario.estanciaDias,
		comentario.dineroGastado,
		comentario.valoracion
	];

	db.run(sqlInsertarComentario, params, function (err) {
		if (err) {
			console.error('Error al insertar comentario:', err.message);
			return;
		}
		// console.log(`Comentario insertado con ID ${this.lastID}`);
	});
}

function generarNumeroAleatorioRedondo(min, max) {
    const minMultiple = Math.ceil(min / 50);
    const maxMultiple = Math.floor(max / 50);
    const randomIndex = Math.floor(Math.random() * (maxMultiple - minMultiple + 1)) + minMultiple;
    return randomIndex * 50;
}

async function poblarComentarios() {
	const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
	db.all(sqlSeleccionarDestinos, async (err, rows) => {
		if (err) {
			console.error('Error al seleccionar los destinos:', err.message);
			return;
		}
		for(const row of rows){
			const destinoIdComentario = row.id
			const numComentarios = generarNumeroAleatorio(11, 17)
			for (var i = 0; i<numComentarios; i++){
				const valoracion = generarNumeroAleatorio(2,5)
				var texto = ""
				if(valoracion < 4){
					texto = textoMediano[Math.floor(Math.random() * textoMediano.length)]
				}
				else{
					texto = textoPositivo[Math.floor(Math.random() * textoPositivo.length)]
				}
				const probabilidadPermiso = 0.7
				const permisoExtraInfo = Math.random() < probabilidadPermiso
				var gastosTotales = 0
				var diasTotal = 0
				if(permisoExtraInfo){
					gastosTotales = generarNumeroAleatorioRedondo(500, 1600)
					diasTotal = Math.floor(gastosTotales/generarNumeroAleatorioRedondo(200, 450))
				}
				const comentario = {
					"usuarioId": generarNumeroAleatorio(1, 6),
					"destinoId": "",
					"texto": texto,
					"permisoExtraInfo": permisoExtraInfo,
					"estanciaDias": diasTotal,
					"dineroGastado": gastosTotales,
					"valoracion": valoracion
				}
				// console.log(comentario)
				// if(destinoIdComentario == 1){
				// 	console.log(comentario)
				// }
				await insertComentario(destinoIdComentario, comentario)
			}
		}


		// for (const row of rows) {
		// 	const destinoId = row.id;
		// 	const repeticiones = 2;
		// 	for (var i = 0; i < 2; i++) {
		// 		for (const comentario of comentariosEjemplo) {
		// 			comentario.texto = comentario.texto + " Id: " + destinoId;
		// 			await insertComentario(destinoId, comentario);
		// 		}
		// 	}
		// }
	});
}


function generarNumeroAleatorio(min, max) {
	const random = Math.random();
	const numeroAleatorio = Math.floor(random * (max - min + 1)) + min;
	return numeroAleatorio;
}

// poblarComentarios();

async function poblarVisitados() {
    const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
    db.all(sqlSeleccionarDestinos, async (err, rows) => {
        if (err) {
            console.error('Error al seleccionar los destinos:', err.message);
            return;
        }
        const usuarioIds = [1, 2, 3, 4];
        for (const row of rows) {
            const destinoId = row.id;
            const fechaVisita = Math.floor(Date.now() / 1000);
            const usuarioId = usuarioIds[Math.floor(Math.random() * usuarioIds.length)];
            await insertVisitado(usuarioId, destinoId, fechaVisita);
        }
    });
}

async function insertVisitado(usuarioId, destinoId, fechaVisita) {
    const sqlInsertarVisitado = `
        INSERT INTO Visitados (usuarioId, destinoId, fechaVisita)
        VALUES (?, ?, ?)
    `;
    const params = [usuarioId, destinoId, fechaVisita];

    db.run(sqlInsertarVisitado, params, function (err) {
        if (err) {
            console.error('Error al insertar visitado:', err.message);
            return;
        }
        // console.log(`Visitado insertado con ID ${this.lastID}`);
    });
}

async function poblarHistoria() {
    const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
    db.all(sqlSeleccionarDestinos, async (err, rows) => {
        if (err) {
            console.error('Error al seleccionar los destinos:', err.message);
            return;
        }
        const usuarioIds = [1, 2, 3, 4];
        for (const row of rows) {
            const destinoId = row.id;
            const fechaEntrado = Math.floor(Date.now() / 1000);
            const usuarioId = usuarioIds[Math.floor(Math.random() * usuarioIds.length)];
            await insertHistorial(usuarioId, destinoId, fechaEntrado);
        }
    });
}

async function insertHistorial(usuarioId, destinoId, fechaEntrado) {
    const sqlInsertarHistorial = `
        INSERT INTO Historial (usuarioId, destinoId, fechaEntrado)
        VALUES (?, ?, ?)
    `;
    const params = [usuarioId, destinoId, fechaEntrado];

    db.run(sqlInsertarHistorial, params, function (err) {
        if (err) {
            console.error('Error al insertar historial:', err.message);
            return;
        }
        // console.log(`Historial insertado con ID ${this.lastID}`);
    });
}

async function poblarFavoritos() {
    const sqlSeleccionarDestinos = `SELECT id FROM Destino`;
    db.all(sqlSeleccionarDestinos, async (err, rows) => {
        if (err) {
            console.error('Error al seleccionar los destinos:', err.message);
            return;
        }
        const usuarioIds = [1, 2, 3, 4];
        for (const row of rows) {
            const destinoId = row.id;
            const usuarioId = usuarioIds[Math.floor(Math.random() * usuarioIds.length)];
            await insertFavorito(usuarioId, destinoId);
        }
    });
}

async function insertFavorito(usuarioId, destinoId) {
    const sqlInsertarFavorito = `
        INSERT INTO Favoritos (usuarioId, destinoId)
        VALUES (?, ?)
    `;
    const params = [usuarioId, destinoId];

    db.run(sqlInsertarFavorito, params, function (err) {
        if (err) {
            console.error('Error al insertar favorito:', err.message);
            return;
        }
        // console.log(`Favorito insertado con ID ${this.lastID}`);
    });
}








module.exports = CheckIfBDNull;