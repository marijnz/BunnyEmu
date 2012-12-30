/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.entities;



/**
 *
 * @author Marijn
 */
public class ClientPacket extends Packet{
    
    public ClientPacket(){
        header = new byte[6];
    }
}
