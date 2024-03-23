import os

def main():
    # Obtener la ruta del directorio actual
    directorio_actual = os.path.dirname(os.path.abspath(__file__))
    
    # Obtener la lista de archivos en el directorio actual
    archivos = os.listdir(directorio_actual)
    
    # Imprimir los nombres de los archivos
    
    print("Archivos en la carpeta actual:")
    aImprimir = ""
    for archivo in archivos:
        if(archivo != "leerNombresFicheros.py"):
            aImprimir = aImprimir + "'"+ archivo +"'," 
            print(archivo)
    print(aImprimir)

if __name__ == "__main__":
    main()