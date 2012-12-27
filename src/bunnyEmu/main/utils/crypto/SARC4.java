/*******************************************************************************
 * Copyright (C) 2012 JMaNGOS <http://jmangos.org/>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package bunnyEmu.main.utils.crypto;

// TODO: Auto-generated Javadoc
/**
 * SARC4, used twice (en/decoding) for WotLK
 */
public class SARC4 {
	
	/** The state. */
	private byte state[] = new byte[256];
    
    /** The x. */
    private int x;
    
    /** The y. */
    private int y;
	
	/**
	 * Inits the.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean init(byte[] key) {

        for (int i=0; i < 256; i++) {
            state[i] = (byte)i;
        }
        
        x = 0;
        y = 0;
        
        int index1 = 0;
        int index2 = 0;
        
        byte tmp;
        
        if (key == null || key.length == 0) {
            throw new NullPointerException();
        }
        
        for (int i=0; i < 256; i++) {

            index2 = ((key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;

            tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            
            index1 = (index1 + 1) % key.length;
        }
		return true;
		
	}
	
	/**
	 * Update.
	 *
	 * @param buf the buf
	 * @return the byte[]
	 */
	public byte[] Update(byte[] buf){

	        int xorIndex;
	        byte tmp;
	        
	        if (buf == null) {
	            return null;
	        }
	        
	        byte[] result = new byte[buf.length];
	        
	        for (int i=0; i < buf.length; i++) {

	            x = (x + 1) & 0xff;
	            y = ((state[x] & 0xff) + y) & 0xff;

	            tmp = state[x];
	            state[x] = state[y];
	            state[y] = tmp;
	            
	            xorIndex = ((state[x] &0xff) + (state[y] & 0xff)) & 0xff;
	            result[i] = (byte)(buf[i] ^ state[xorIndex]);
	        }
	        
	        return result;
	    }
	}
