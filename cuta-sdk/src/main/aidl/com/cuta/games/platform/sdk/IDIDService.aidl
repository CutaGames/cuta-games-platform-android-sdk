// IDIDService.aidl
package com.cuta.games.platform.sdk;

// Declare any non-default types here with import statements

interface IDIDService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   // void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
      //      double aDouble, String aString);
    String getCurrentDID();
    boolean isDIDValid(String did);
}