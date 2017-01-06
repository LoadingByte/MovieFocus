
package de.unratedfilms.moviefocus.shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Consts {

    public static final String MOD_ID      = "moviefocus";
    public static final String MOD_NAME    = "MovieFocus";
    public static final String MOD_VERSION = "@MOD_VERSION@";               // Replaced during build

    public static final Logger LOGGER      = LogManager.getLogger(MOD_NAME);

    private Consts() {

    }

}
