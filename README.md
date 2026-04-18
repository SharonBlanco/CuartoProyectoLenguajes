🎮 Plataforma de Juegos — TEC San Carlos
Programación Orientada a Objetos · Prof. Oscar Víquez Acuña
Plataforma modular de juegos desarrollada en Java con interfaz estilo pixel-art retro. Permite cargar juegos como módulos JAR independientes y los detecta automáticamente sin reiniciar la aplicación.
 Estructura del repositorio
/
├── Platforma/          # Proyecto principal (NetBeans/Maven)
│   ├── src/main/java/
│   │   ├── core/       # Lógica central: interfaces, gestor, loader, watcher
│   │   └── ui/         # Interfaz gráfica principal
│   └── juegos/         # Carpeta donde se colocan los JARs de juegos
│
├── TicTacToe/          # Juego 1 — proyecto independiente
├── Ahorcado/           # Juego 2 — proyecto independiente
└── Snake/              # Juego 3 — proyecto independiente


 Cómo correr el proyecto
Desde NetBeans
1.	File → Open Project y abrí la carpeta Platforma/
2.	Click derecho en el proyecto → Run
Desde terminal
cd Platforma
java -jar Platforma.jar

La carpeta juegos/ debe estar en el mismo directorio desde donde corrés la aplicación. Los JARs de los juegos ya vienen incluidos ahí.

Juegos incluidos
Juego	Descripción	Controles
Tic Tac Toe	Tres en línea clásico para 2 jugadores	Click en las celdas
Ahorcado	Adivina palabras de programación	Click en las letras
Snake	La serpiente clásica	Flechas del teclado

 Agregar o quitar juegos en tiempo de ejecución
La plataforma monitorea automáticamente la carpeta juegos/ mientras está corriendo.
·	Agregar un juego: copiá el .jar a la carpeta juegos/ → aparece en el menú en segundos
·	Quitar un juego: borrá el .jar de la carpeta juegos/ → desaparece del menú automáticamente
No hace falta reiniciar ni tocar nada.

🔧 Compilar un juego nuevo
Cada juego es un proyecto Maven independiente. Para compilarlo y generar el JAR:
cd TicTacToe       # o Ahorcado, Snake, o tu juego nuevo
mvn clean package

El JAR generado queda en target/. Copialo a Platforma/juegos/ y la plataforma lo detecta solo.

Crear tu propio juego
Todo juego debe implementar la interfaz JuegoPlugin del paquete core:
package plugin;

import core.*;
import javax.swing.JPanel;

public class Juego implements JuegoPlugin {

    private GameObserver observer;

    @Override
    public String getNombre() { return "Mi Juego"; }

    @Override
    public JPanel getVista() { /* retorná tu panel de juego */ }

    @Override
    public void iniciar() { /* reiniciar estado del juego */ }

    @Override
    public void setObservador(GameObserver observer) {
        this.observer = observer;
    }

    // Cuando el juego termina, notificá a la plataforma:
    private void terminar(int puntaje) {
        if (observer != null)
            observer.juegoTerminado(getNombre(), puntaje);
    }
}

Importante: la clase principal del juego debe llamarse plugin.Juego para que el Loader la encuentre.

Patrones de diseño implementados
Patrón	Dónde se aplica
Observer	GameObserver — la plataforma recibe notificaciones asincrónicas cuando un juego termina
Singleton	GameManager — no permite cargar dos instancias del mismo juego
Iterator	Verificación del tablero en Tic Tac Toe · teclado y colisiones en Snake
MVC	core/ = Modelo · ui/ = Vista · GameManager = Controlador


Persistencia de puntajes
Los puntajes se guardan automáticamente en un archivo scores.dat en el directorio de trabajo. El top 3 por juego persiste entre sesiones.
 Requisitos
·	Java 11 o superior
·	NetBeans 12+ (opcional, también funciona con Maven desde terminal)

 Autores
Sharon Blanco Piedra

Instituto Tecnológico de Costa Rica — Sede San Carlos

