import os
country_codes = {
    'AD': 'Andorra',
    'AE': 'Emiratos Árabes Unidos',
    'AF': 'Afganistán',
    'AG': 'Antigua y Barbuda',
    'AI': 'Anguila',
    'AL': 'Albania',
    'AM': 'Armenia',
    'AN': 'Antillas Neerlandesas',
    'AO': 'Angola',
    'AQ': 'Antártida',
    'AR': 'Argentina',
    'AS': 'Samoa Americana',
    'AT': 'Austria',
    'AU': 'Australia',
    'AW': 'Aruba',
    'AX': 'Islas Åland',
    'AZ': 'Azerbaiyán',
    'BA': 'Bosnia y Herzegovina',
    'BB': 'Barbados',
    'BD': 'Bangladesh',
    'BE': 'Bélgica',
    'BF': 'Burkina Faso',
    'BG': 'Bulgaria',
    'BH': 'Bahréin',
    'BI': 'Burundi',
    'BJ': 'Benin',
    'BL': 'San Bartolomé',
    'BM': 'Bermudas',
    'BN': 'Brunéi',
    'BO': 'Bolivia',
    'BR': 'Brasil',
    'BS': 'Bahamas',
    'BT': 'Bhután',
    'BV': 'Isla Bouvet',
    'BW': 'Botsuana',
    'BY': 'Belarús',
    'BZ': 'Belice',
    'CA': 'Canadá',
    'CC': 'Islas Cocos',
    'CF': 'República Centro-Africana',
    'CG': 'Congo',
    'CH': 'Suiza',
    'CI': 'Costa de Marfil',
    'CK': 'Islas Cook',
    'CL': 'Chile',
    'CM': 'Camerún',
    'CN': 'China',
    'CO': 'Colombia',
    'CR': 'Costa Rica',
    'CU': 'Cuba',
    'CV': 'Cabo Verde',
    'CX': 'Islas Christmas',
    'CY': 'Chipre',
    'CZ': 'República Checa',
    'DE': 'Alemania',
    'DJ': 'Yibuti',
    'DK': 'Dinamarca',
    'DM': 'Dominica',
    'DO': 'República Dominicana',
    'DZ': 'Argelia',
    'EC': 'Ecuador',
    'EE': 'Estonia',
    'EG': 'Egipto',
    'EH': 'Sahara Occidental',
    'ER': 'Eritrea',
    'ES': 'España',
    'ET': 'Etiopía',
    'FI': 'Finlandia',
    'FJ': 'Fiji',
    'FK': 'Islas Malvinas',
    'FM': 'Micronesia',
    'FO': 'Islas Faroe',
    'FR': 'Francia',
    'GA': 'Gabón',
    'GB': 'Reino Unido',
    'GD': 'Granada',
    'GE': 'Georgia',
    'GF': 'Guayana Francesa',
    'GG': 'Guernsey',
    'GH': 'Ghana',
    'GI': 'Gibraltar',
    'GL': 'Groenlandia',
    'GM': 'Gambia',
    'GN': 'Guinea',
    'GP': 'Guadalupe',
    'GQ': 'Guinea Ecuatorial',
    'GR': 'Grecia',
    'GS': 'Georgia del Sur e Islas Sandwich del Sur',
    'GT': 'Guatemala',
    'GU': 'Guam',
    'GW': 'Guinea-Bissau',
    'GY': 'Guayana',
    'HK': 'Hong Kong',
    'HM': 'Islas Heard y McDonald',
    'HN': 'Honduras',
    'HR': 'Croacia',
    'HT': 'Haití',
    'HU': 'Hungría',
    'ID': 'Indonesia',
    'IE': 'Irlanda',
    'IL': 'Israel',
    'IM': 'Isla de Man',
    'IN': 'India',
    'IO': 'Territorio Británico del Océano Índico',
    'IQ': 'Irak',
    'IR': 'Irán',
    'IS': 'Islandia',
    'IT': 'Italia',
    'JE': 'Jersey',
    'JM': 'Jamaica',
    'JO': 'Jordania',
    'JP': 'Japón',
    'KE': 'Kenia',
    'KG': 'Kirguistán',
    'KH': 'Camboya',
    'KI': 'Kiribati',
    'KM': 'Comoros',
    'KN': 'San Cristóbal y Nieves',
    'KP': 'Corea del Norte',
    'KR': 'Corea del Sur',
    'KW': 'Kuwait',
    'KY': 'Islas Caimán',
    'KZ': 'Kazajistán',
    'LA': 'Laos',
    'LB': 'Líbano',
    'LC': 'Santa Lucía',
    'LI': 'Liechtenstein',
    'LK': 'Sri Lanka',
    'LR': 'Liberia',
    'LS': 'Lesotho',
    'LT': 'Lituania',
    'LU': 'Luxemburgo',
    'LV': 'Letonia',
    'LY': 'Libia',
    'MA': 'Marruecos',
    'MC': 'Mónaco',
    'MD': 'Moldova',
    'ME': 'Montenegro',
    'MG': 'Madagascar',
    'MH': 'Islas Marshall',
    'MK': 'Macedonia',
    'ML': 'Mali',
    'MM': 'Myanmar',
    'MN': 'Mongolia',
    'MO': 'Macao',
    'MQ': 'Martinica',
    'MR': 'Mauritania',
    'MS': 'Montserrat',
    'MT': 'Malta',
    'MU': 'Mauricio',
    'MV': 'Maldivas',
    'MW': 'Malawi',
    'MX': 'México',
    'MY': 'Malasia',
    'MZ': 'Mozambique',
    'NA': 'Namibia',
    'NC': 'Nueva Caledonia',
    'NE': 'Níger',
    'NF': 'Islas Norfolk',
    'NG': 'Nigeria',
    'NI': 'Nicaragua',
    'NL': 'Países Bajos',
    'NO': 'Noruega',
    'NP': 'Nepal',
    'NR': 'Nauru',
    'NU': 'Niue',
    'NZ': 'Nueva Zelanda',
    'OM': 'Omán',
    'PA': 'Panamá',
    'PE': 'Perú',
    'PF': 'Polinesia Francesa',
    'PG': 'Papúa Nueva Guinea',
    'PH': 'Filipinas',
    'PK': 'Pakistán',
    'PL': 'Polonia',
    'PM': 'San Pedro y Miquelón',
    'PN': 'Islas Pitcairn',
    'PR': 'Puerto Rico',
    'PS': 'Palestina',
    'PT': 'Portugal',
    'PW': 'Islas Palaos',
    'PY': 'Paraguay',
    'QA': 'Qatar',
    'RE': 'Reunión',
    'RO': 'Rumanía',
    'RS': 'Serbia',
    'RU': 'Rusia',
    'RW': 'Ruanda',
    'SA': 'Arabia Saudita',
    'SB': 'Islas Salomón',
    'SC': 'Seychelles',
    'SD': 'Sudán',
    'SE': 'Suecia',
    'SG': 'Singapur',
    'SH': 'Santa Elena',
    'SI': 'Eslovenia',
    'SJ': 'Islas Svalbard y Jan Mayen',
    'SK': 'Eslovaquia',
    'SL': 'Sierra Leona',
    'SM': 'San Marino',
    'SN': 'Senegal',
    'SO': 'Somalia',
    'SR': 'Surinam',
    'ST': 'Santo Tomé y Príncipe',
    'SV': 'El Salvador',
    'SY': 'Siria',
    'SZ': 'Suazilandia',
    'TC': 'Islas Turcas y Caicos',
    'TD': 'Chad',
    'TF': 'Territorios Australes Franceses',
    'TG': 'Togo',
    'TH': 'Tailandia',
    'TJ': 'Tayikistán',
    'TK': 'Tokelau',
    'TL': 'Timor-Leste',
    'TM': 'Turkmenistán',
    'TN': 'Túnez',
    'TO': 'Tonga',
    'TR': 'Turquía',
    'TT': 'Trinidad y Tobago',
    'TV': 'Tuvalu',
    'TZ': 'Tanzania',
    'UA': 'Ucrania',
    'UG': 'Uganda',
    'US': 'Estados Unidos de América',
    'UY': 'Uruguay',
    'UZ': 'Uzbekistán',
    'VA': 'Ciudad del Vaticano',
    'VC': 'San Vicente y las Granadinas',
    'VE': 'Venezuela',
    'VG': 'Islas Vírgenes Británicas',
    'VI': 'Islas Vírgenes de los Estados Unidos de América',
    'VN': 'Vietnam',
    'VU': 'Vanuatu',
    'WF': 'Wallis y Futuna',
    'WS': 'Samoa',
    'YE': 'Yemen',
    'YT': 'Mayotte',
    'ZA': 'Sudáfrica',
    'BQ': 'Bonaire, San Eustaquio y Saba',
    'CD': 'República Democrática del Congo',
    'CW': 'Curazao',
    'MF': 'San Martín (parte francesa)',
    'MP': 'Islas Marianas del Norte',
    'SS': 'Sudán del Sur',
    'SX': 'San Martín (parte neerlandesa)',
    'UM': 'Territorios Ultramarinos de los Estados Unidos',
    'XK': 'Kosovo',
    'ZM': 'Zambia',
    'ZW': 'Zimbabue'
}
directory = './h60'

def rename_files_in_directory(directory):
    # Obtener una lista de todos los archivos en el directorio
    for filename in os.listdir(directory):
        # Construir la ruta completa del archivo
        old_file_path = os.path.join(directory, filename)
        
        # Asegurarse de que sea un archivo y no un directorio
        if os.path.isfile(old_file_path):
            # Reemplazar comas con nada y espacios con guiones bajos
            new_filename = filename.replace(',', '').replace(' ', '_')
            
            # Construir la nueva ruta del archivo
            new_file_path = os.path.join(directory, new_filename)
            
            # Renombrar el archivo
            os.rename(old_file_path, new_file_path)
            print(f'Renombrado: {old_file_path} -> {new_file_path}')

# Ruta al directorio que contiene los archivos

# Llamar a la función
#rename_files_in_directory(directory)

def save_filenames_to_txt(directory, output_file):
    # Obtener una lista de todos los archivos en el directorio
    filenames = [filename for filename in os.listdir(directory) if os.path.isfile(os.path.join(directory, filename))]
    
    # Guardar los nombres de los archivos en un archivo de texto
    with open(output_file, 'w', encoding='utf-8') as file:
        for filename in filenames:
            file.write(f"{filename}\n")

    print(f'Lista de archivos guardada en {output_file}')

# Ruta al directorio que contiene los archivos

# Ruta al archivo de salida
output_file_path = 'output_filenames.txt'

save_filenames_to_txt(directory, output_file_path)

# # Recorre todos los archivos en el directorio
# for filename in os.listdir(directory):
#     # Asegúrate de que estamos trabajando solo con archivos PNG
#     if filename.endswith('.png'):
#         # Obtén el código de país del nombre del archivo (asume que el formato es 'XX.png' o 'XX-XXX.png')
#         country_code = filename[:-4].upper()  # Elimina la extensión .png y convierte a mayúsculas
        
#         # Busca el nombre del país en el diccionario
#         if country_code in country_codes:
#             country_name = country_codes[country_code]
#             # Construye el nuevo nombre de archivo
#             new_filename = f'ic_flag_{country_name}.png'
#             # Obtén las rutas completas al archivo antiguo y al nuevo archivo
#             old_file = os.path.join(directory, filename)
#             new_file = os.path.join(directory, new_filename)
#             # Renombra el archivo
#             os.rename(old_file, new_file)
#             print(f'Renamed {old_file} to {new_file}')
#         #else:
#             #print(f'Country code {country_code} not found in dictionary')