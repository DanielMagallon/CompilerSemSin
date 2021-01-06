# Compilador U3

Equipo: 
    Magallon Villanueva Edgar Daniel
    Maldonado Mendoza Roberto Carlos
    
Notas:
    1)  
        Para en caso de no poder ejecutar el programa desde el IDE eclipse o incluso
        el netbeans, descargar el IDE intelli j idea (de la empresa JetBrains) del siguiente enlace:
        https://www.jetbrains.com/idea/download/
        Desconocemos que problemas haya podido haber al ejecutar en otro IDE el software
        Por lo que lo mas recomendable es usar el IDE que proporcionamos.
        
   2)
        El programa puede no funcionar debido a que hacer uso de la serializacion de objetos
        y de archivos para extraer los datos necesarios para su funcionamiento.
        
            Primero hay que agregar las bibliotecas (jar) de la ruta ~/GramaticaU3/src/Apache POI
            al proyecto para que puedan ser reconocidas.
            Estas librerias son utiles ya que extraen los datos de excel como la tabla de analisis
            sintactico, y algunas funcionalidades mas.

        Para que el programa cargue los datos y pueda funcionar hay que ir al archivo 
        ~/GramaticaU3/src/Managers/Loader.java y ejecutarlo, en caso de que haya error
        posiblemente se deba a las librerias de Apache POI     
            
