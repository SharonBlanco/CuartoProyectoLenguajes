markdown# Plataforma de Juegos — TEC San Carlos

Plataforma modular de juegos en Java con interfaz estilo pixel-art retro. Los juegos se cargan como modulos JAR independientes y se detectan automaticamente sin reiniciar la aplicacion.

---

## Estructura del repositorio
/
├── Platforma/          # Proyecto principal (NetBeans/Maven)
│   ├── src/main/java/
│   │   ├── core/       # Interfaces, gestor, loader, watcher
│   │   └── ui/         # Interfaz grafica
│   └── juegos/         # Carpeta donde van los JARs
│
├── TicTacToe/          # Juego 1
├── Ahorcado/           # Juego 2
└── Snake/              # Juego 3

---

## Como correr el proyecto

**Desde NetBeans**

`File → Open Project`, abrí la carpeta `Platforma/` y dale Run.

**Desde terminal**

```bash
cd Platforma
java -jar Platforma.jar
```

La carpeta `juegos/` tiene que estar en el mismo directorio desde donde corres la aplicacion. Los JARs ya vienen incluidos.

---

## Juegos incluidos

| Juego | Descripcion | Controles |
|-------|-------------|-----------|
| Tic Tac Toe | Tres en linea para 2 jugadores | Click en las celdas |
| Ahorcado | Adivina palabras de programacion | Click en las letras |
| Snake | La serpiente clasica | Flechas del teclado |

---

## Agregar o quitar juegos en tiempo de ejecucion

La plataforma monitorea la carpeta `juegos/` mientras esta corriendo.

- Para agregar un juego: copia el `.jar` a la carpeta `juegos/` y aparece en el menu en segundos.
- Para quitarlo: borra el `.jar` y desaparece del menu automaticamente.

No hace falta reiniciar.

---

## Compilar un juego

```bash
cd TicTacToe   # o Ahorcado, Snake, etc.
mvn clean package
```

El JAR queda en `target/`. Copialo a `Platforma/juegos/`.

---

## Crear un juego propio

La clase principal del juego debe llamarse `plugin.Juego` e implementar `JuegoPlugin`:

```java
package plugin;

import core.*;
import javax.swing.JPanel;

public class Juego implements JuegoPlugin {

    private GameObserver observer;

    @Override
    public String getNombre() { return "Mi Juego"; }

    @Override
    public JPanel getVista() { /* tu panel de juego */ }

    @Override
    public void iniciar() { /* reiniciar estado */ }

    @Override
    public void setObservador(GameObserver observer) {
        this.observer = observer;
    }

    private void terminar(int puntaje) {
        if (observer != null)
            observer.juegoTerminado(getNombre(), puntaje);
    }
}
```

---

## Patrones de diseno implementados

| Patron | Donde |
|--------|-------|
| Observer | `GameObserver` — la plataforma recibe notificacion cuando un juego termina |
| Singleton | `GameManager` — no permite cargar dos instancias del mismo juego |
| Iterator | Verificacion del tablero en Tic Tac Toe, teclado y colisiones en Snake |
| MVC | `core/` modelo, `ui/` vista, `GameManager` controlador |

---

## Persistencia

Los puntajes se guardan en `scores.dat` en el directorio de trabajo. El top 3 por juego sobrevive entre sesiones.

---

## Requisitos

- Java 11 o superior
- NetBeans 12+ (opcional)

---

## Autores

> Sharon Blanco Piedra
