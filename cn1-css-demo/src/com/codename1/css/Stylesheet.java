/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.css;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author shannah
 */
public class Stylesheet {
    private static int MODE_GLOBAL=1;
    private static int MODE_SELECTOR=2;
    private static int MODE_BLOCK=3;
    private static int MODE_PROPERTY_KEY=4;
    private static int MODE_PROPERTY_VAL=5;
    
    
    private void parse(Reader reader) throws IOException {
        char c = (char)reader.read();
        
    }
}
