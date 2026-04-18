/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import javax.swing.JPanel;

public interface JuegoPlugin {

    String getNombre();

    JPanel getVista(); // IMPORTANTE: GUI integrada

    void iniciar();

    void setObservador(GameObserver observer);

}